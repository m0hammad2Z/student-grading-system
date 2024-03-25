package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.User;
import org.example.service.CourseService;

import java.util.ArrayList;
import java.util.List;

public class EnrollStudentStrategy implements ActionStrategy{
    private CourseService courseService;
    private User instructor;

    public EnrollStudentStrategy(CourseService courseService, User instructor){
        this.courseService = courseService;
        this.instructor = instructor;
    }

    @Override
    public Message execute(Message message) {
        int courseId;
        String studentIds;
        Message.Builder responseBuilder = new Message.Builder();
        try {
            courseId = Integer.parseInt((String) message.getData().get("course_id").getContent());
            studentIds = (String) message.getData().get("student_ids").getContent();
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid course ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        List<Integer> studentIdList = new ArrayList<>();
        try {
            String[] studentIdArray = studentIds.split(",");
            for (String studentId : studentIdArray) {
                studentIdList.add(Integer.parseInt(studentId));
            }
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid student ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        try {
            courseService.enrollStudent(studentIdList, courseId, instructor.getId());
            responseBuilder.withData("success", new MessageData("Students enrolled successfully", DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to enroll students", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("course_id", new MessageData("Enter course ID", DataMode.INPUT, true))
                .withData("student_ids", new MessageData("Enter student IDs (comma separated)", DataMode.INPUT, true))
                .build();
    }
}