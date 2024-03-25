package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Course;
import org.example.service.CourseService;

public class CreateCourseStrategy implements ActionStrategy{
    private final CourseService courseService;

    public CreateCourseStrategy(CourseService courseService){
        this.courseService = courseService;
    }

    @Override
    public Message execute(Message message) {
        String courseName = (String) message.getData().get("courseName").getContent();
        Message.Builder responseBuilder = new Message.Builder();
        try {
            courseService.create(new Course(courseName));
            responseBuilder.withData("success", new MessageData("Course created successfully", DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to create course", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData(){
        return new Message.Builder()
                .withData("courseName", new MessageData("Enter course name", DataMode.INPUT, true))
                .build();
    }

}
