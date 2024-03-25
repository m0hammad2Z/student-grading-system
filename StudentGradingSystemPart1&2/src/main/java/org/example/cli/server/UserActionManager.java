package org.example.cli.server;

import org.example.cli.actions.ActionStrategy;
import org.example.model.*;
import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;

import java.util.HashMap;
import java.util.Map;

import org.example.cli.actions.*;

public class UserActionManager {
    protected ClientHandler clientHandler;
    protected User user;

    private final Map<Option, ActionStrategy> studentActionMap = new HashMap<>();
    private final Map<Option, ActionStrategy> adminActionMap = new HashMap<>();
    private final Map<Option, ActionStrategy> instructorActionMap = new HashMap<>();


    public UserActionManager(ClientHandler clientHandler, User user) {
        this.clientHandler = clientHandler;
        this.user = user;

        adminActionMap.put(Option.CREATE_USER, new CreateUserStrategy(clientHandler.getUserService(), clientHandler.getRoleService()));
        adminActionMap.put(Option.CREATE_COURSE, new CreateCourseStrategy(clientHandler.getCourseService()));
        adminActionMap.put(Option.UPDATE_USER, new UpdateUserStrategy(clientHandler.getUserService(), clientHandler.getRoleService()));
        adminActionMap.put(Option.UPDATE_COURSE, new UpdateCourseStrategy(clientHandler.getCourseService()));
        adminActionMap.put(Option.DELETE_USER, new DeleteUserStrategy(clientHandler.getUserService()));
        adminActionMap.put(Option.DELETE_COURSE, new DeleteCourseStrategy(clientHandler.getCourseService()));
        adminActionMap.put(Option.VIEW_USERS, new ViewUsersStrategy(clientHandler.getUserService()));
        adminActionMap.put(Option.VIEW_COURSES, new ViewCoursesStrategy(clientHandler.getCourseService()));
        adminActionMap.put(Option.ASSIGN_INSTRUCTOR, new AssignInstructorStrategy(clientHandler.getCourseService()));


        instructorActionMap.put(Option.ENROLL_STUDENT, new EnrollStudentStrategy(clientHandler.getCourseService(),user));
        instructorActionMap.put(Option.REMOVE_STUDENT, new RemoveStudentStrategy(clientHandler.getCourseService(),user));
        instructorActionMap.put(Option.REMOVE_ASSESSMENT, new RemoveAssessmentStrategy(clientHandler.getAssessmentService()));
        instructorActionMap.put(Option.CREATE_ASSESSMENT, new CreateAssessmentStrategy(clientHandler.getAssessmentService()));
        instructorActionMap.put(Option.UPDATE_ASSESSMENT, new UpdateAssessmentStrategy(clientHandler.getAssessmentService()));
        instructorActionMap.put(Option.VIEW_ASSESSMENTS, new ViewAssessmentsStrategy(clientHandler.getAssessmentService()));
        instructorActionMap.put(Option.GRADE_ASSESSMENT, new GradeAssessmentStrategy(clientHandler.getGradeService(), user));
        instructorActionMap.put(Option.VIEW_INSTRUCTOR_COURSES, new ViewInstructorCoursesStrategy(clientHandler.getCourseService(), user));


        studentActionMap.put(Option.VIEW_STUDENT_COURSES, new ViewStudentCoursesStrategy(clientHandler.getCourseService(), user));
        studentActionMap.put(Option.VIEW_STUDENT_GRADES, new ViewStudentGradesStrategy(clientHandler.getGradeService(), user));

    }

    public void execute() {
        while (true) {
            sendOptions();
            Message response = clientHandler.getMessenger().receive();
            if (response.getData().containsKey("choice") && response.getData().get("choice").getContent().equals("e")) {
                break;
            }
            try {
                int choiceIndex = -1;
                if (response.getData().containsKey("choice")) {
                    choiceIndex = Integer.parseInt((String) response.getData().get("choice").getContent());
                }
                handleUserChoice(choiceIndex);
            } catch (NumberFormatException e) {
                clientHandler.getMessenger().send(new Message.Builder().
                        withData("error", new MessageData("Invalid option 1", DataMode.OUTPUT, true)).
                        build());
            }
        }
    }

    private void handleUserChoice(int choiceIndex) {
        ActionStrategy action = getActionStrategy(choiceIndex);
        if(action == null){
            clientHandler.getMessenger().send(new Message.Builder().
                    withData("error", new MessageData("Invalid option 2", DataMode.OUTPUT, true)).
                    build());
            return;
        }
        executeAction(action);
    }

    private ActionStrategy getActionStrategy(int choiceIndex) {
        switch (user.getRole().getName()) {
            case "STUDENT":
                return getActionByIndex(choiceIndex, studentActionMap);
            case "ADMIN":
                return getActionByIndex(choiceIndex, adminActionMap);
            case "INSTRUCTOR":
                return getActionByIndex(choiceIndex, instructorActionMap);
            default:
                return null;
        }
    }

    private void executeAction(ActionStrategy action) {
        Message preparedData = action.getPreparedData();
        if (preparedData != null) {
            clientHandler.getMessenger().send(preparedData);
        } else {
            return;
        }

        // Receive response from client only if there are any inputs.
        boolean hasInput = preparedData.getData().values().stream().anyMatch(data -> data.getStatus() == DataMode.INPUT);

        if (hasInput) {
            Message response = clientHandler.getMessenger().receive();
            Message result = action.execute(response);
            if (result != null) {
                clientHandler.getMessenger().send(result);
            }
        }

    }

    private ActionStrategy getActionByIndex(int index, Map<Option, ActionStrategy> actionMap) {
        if (index < 0 || index >= actionMap.size()) {
            return null;
        }
        Option option = (Option) actionMap.keySet().toArray()[index];
        return actionMap.get(option);
    }

    private void sendOptions(){
        StringBuilder options = new StringBuilder();
        switch (user.getRole().getName()) {
            case "STUDENT":
                for (int i = 0; i < studentActionMap.size(); i++) {
                    options.append(i).append(". ").append(studentActionMap.keySet().toArray()[i]).append("\n");
                }
                break;
            case "ADMIN":
                for (int i = 0; i < adminActionMap.size(); i++) {
                    options.append(i).append(". ").append(adminActionMap.keySet().toArray()[i]).append("\n");
                }
                break;
            case "INSTRUCTOR":
                for (int i = 0; i < instructorActionMap.size(); i++) {
                    options.append(i).append(". ").append(instructorActionMap.keySet().toArray()[i]).append("\n");
                }
                break;
        }

        options.append("e. Exit");

        clientHandler.getMessenger().send(new Message.Builder().
                withData("options", new MessageData(options.toString(), DataMode.OUTPUT, true)).
                withData("choice", new MessageData("Enter your choice", DataMode.INPUT, true)).
                build());
    }
}