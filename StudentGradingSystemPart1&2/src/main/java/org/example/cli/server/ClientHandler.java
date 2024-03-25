package org.example.cli.server;

import java.io.IOException;
import java.net.Socket;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.cli.util.Messenger;
import org.example.dao.AssessmentDAOImpl;
import org.example.dao.CourseDAOImpl;
import org.example.dao.GradeDAOImpl;
import org.example.dao.RoleDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.model.User;
import org.example.service.AssessmentService;
import org.example.service.CourseService;
import org.example.service.GradeService;
import org.example.service.RoleService;
import org.example.service.UserService;

public class ClientHandler extends Thread {

    private final Socket clientSocket;
    private final Messenger messenger;

    private final UserService userService;
    private final RoleService roleService;
    private final CourseService courseService;
    private final AssessmentService assessmentService;
    private final GradeService gradeService;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.messenger = new Messenger(clientSocket);

        // Initialize services with their respective DAOs
        this.roleService = new RoleService(new RoleDAOImpl());
        this.userService = new UserService(new UserDAOImpl(), this.roleService);
        this.courseService = new CourseService(new CourseDAOImpl());
        this.assessmentService = new AssessmentService(new AssessmentDAOImpl());
        this.gradeService = new GradeService(new GradeDAOImpl());
    }

    @Override
    public void run() {
        try {
            User user = handleLogin();
            if (user != null) {
                UserActionManager userHandle = new UserActionManager(this, user);
                userHandle.execute();
            }
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }

    private User handleLogin() {
        boolean isAuthenticated = false;
        while (!isAuthenticated) {
            // Build and send login message
            Message loginMessage = buildLoginMessage();
            messenger.send(loginMessage);

            // Receive response and extract data
            Message receivedMessage = messenger.receive();
            String email, password;
            try {
                email = (String) receivedMessage.getData().get("email").getContent();
                password = (String) receivedMessage.getData().get("password").getContent();
                if (email == null || password == null) {
                    throw new Exception("Email or password not provided");
                }
                return userService.login(email, password);
            } catch (Exception e) {
                // Send error message and continue loop
                messenger.send(buildErrorMessage(e.getMessage()));
            }
        }
        return null; // Shouldn't be reached due to loop condition
    }

    private Message buildLoginMessage() {
        return new Message.Builder()
                .withData("email", new MessageData("Enter your email", DataMode.INPUT, true))
                .withData("password", new MessageData("Enter your password", DataMode.INPUT, true))
                .build();
    }

    private Message buildErrorMessage(String message) {
        return new Message.Builder()
                .withData("error", new MessageData(message, DataMode.OUTPUT, true))
                .build();
    }

    // Getters for accessing services and messenger
    public Messenger getMessenger() {
        return messenger;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public UserService getUserService() {
        return userService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public AssessmentService getAssessmentService() {
        return assessmentService;
    }

    public GradeService getGradeService() {
        return gradeService;
    }
}