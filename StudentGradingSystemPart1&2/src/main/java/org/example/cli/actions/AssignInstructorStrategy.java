package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.service.CourseService;

import java.util.ArrayList;
import java.util.List;

public class AssignInstructorStrategy implements ActionStrategy {
    private CourseService courseService;

    public AssignInstructorStrategy(CourseService courseService){
        this.courseService = courseService;
    }

    @Override
    public Message execute(Message message) {
        int courseId;
        String instructorIds;
        Message.Builder responseBuilder = new Message.Builder();
        try {
            courseId = Integer.parseInt((String) message.getData().get("course_id").getContent());
            instructorIds = (String) message.getData().get("instructor_ids").getContent();
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid course ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        List<Integer> instructorIdList = new ArrayList<>();
        try {
            String[] instructorIdArray = instructorIds.split(",");
            for (String instructorId : instructorIdArray) {
                instructorIdList.add(Integer.parseInt(instructorId));
            }
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid instructor ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        try {
            courseService.assignInstructor(instructorIdList, courseId);
            responseBuilder.withData("success", new MessageData("Instructors assigned successfully", DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to assign instructors", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("course_id", new MessageData("Enter course ID", DataMode.INPUT, true))
                .withData("instructor_ids", new MessageData("Enter instructor IDs (comma separated)", DataMode.INPUT, true))
                .build();
    }



}
