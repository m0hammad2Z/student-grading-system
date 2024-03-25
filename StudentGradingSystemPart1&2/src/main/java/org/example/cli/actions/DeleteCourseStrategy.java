package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.service.CourseService;

public class DeleteCourseStrategy implements ActionStrategy{
    private CourseService courseService;

    public DeleteCourseStrategy(CourseService courseService){
        this.courseService = courseService;
    }

    @Override
    public Message execute(Message message) {
        int courseId;

        Message.Builder responseBuilder = new Message.Builder();

        try {
            courseId = Integer.parseInt((String) message.getData().get("course_id").getContent());
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid course ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }

        try {
            courseService.delete(courseId);

            responseBuilder.withData("success", new MessageData("Course deleted successfully", DataMode.OUTPUT, false));
        }
        catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        }catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to delete course", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("course_id", new MessageData("Enter course ID", DataMode.INPUT, true))
                .build();
    }

}
