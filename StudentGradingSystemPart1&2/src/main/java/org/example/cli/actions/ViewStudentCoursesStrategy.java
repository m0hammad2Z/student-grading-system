package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.User;
import org.example.service.CourseService;

public class ViewStudentCoursesStrategy implements ActionStrategy{
    private CourseService courseService;
    private User student;

    public ViewStudentCoursesStrategy(CourseService courseService, User student){
        this.courseService = courseService;
        this.student = student;
    }

    @Override
    public Message  getPreparedData(){
        Message.Builder responseBuilder = new Message.Builder();
        try {
            StringBuilder courses = new StringBuilder();
            courseService.getAllStudentCourses(student.getId()).forEach(course -> courses.append(course).append("\n"));
            responseBuilder.withData("courses", new MessageData(courses.toString(), DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to retrieve courses", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message  execute(Message message){
        return new Message.Builder()
                .withData("empty", new MessageData("", DataMode.OUTPUT, false))
                .build();
    }
}