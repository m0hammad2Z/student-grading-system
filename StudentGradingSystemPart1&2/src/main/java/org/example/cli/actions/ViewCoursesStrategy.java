package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Course;
import org.example.service.CourseService;

public class ViewCoursesStrategy implements ActionStrategy{
    private CourseService courseService;

    public ViewCoursesStrategy(CourseService courseService){
        this.courseService = courseService;
    }

    @Override
    public Message execute(Message message) {
        return new Message.Builder()
                .withData("empty", new MessageData("", DataMode.OUTPUT, false))
                .build();
    }

    @Override
    public Message getPreparedData() {
        Message.Builder responseBuilder = new Message.Builder();
        try {
            StringBuilder courses = new StringBuilder();
            for (Course course : courseService.getAll()) {
                courses.append(course).append("\n");
            }
            responseBuilder.withData("courses", new MessageData(courses.toString(), DataMode.OUTPUT, false));
        }catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to retrieve courses", DataMode.OUTPUT, false));
        }

        return responseBuilder.build();

    }

}
