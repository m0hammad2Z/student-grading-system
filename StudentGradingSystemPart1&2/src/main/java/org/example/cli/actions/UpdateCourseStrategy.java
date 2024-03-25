package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Course;
import org.example.service.CourseService;

public class UpdateCourseStrategy implements ActionStrategy{
    private CourseService courseService;

    public UpdateCourseStrategy(CourseService courseService){
        this.courseService = courseService;
    }

    @Override
    public Message execute(Message message) {
        int courseId;
        String courseName = (String) message.getData().get("courseName").getContent();

        Message.Builder responseBuilder = new Message.Builder();

        try {
            courseId = Integer.parseInt((String) message.getData().get("course_id").getContent());
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid course ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }

        try {
            Course course = courseService.getById(courseId);
            courseName = courseName.isEmpty() ? course.getName() : courseName;
            course.setName(courseName);
            courseService.update(course);

            responseBuilder.withData("success", new MessageData("Course updated successfully", DataMode.OUTPUT, false));
        }
        catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        }catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to update course", DataMode.OUTPUT, false));
        }

        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData(){
        return new Message.Builder()
                .withData("course_id", new MessageData("Enter course ID", DataMode.INPUT, false))
                .withData("courseName", new MessageData("Enter course name", DataMode.INPUT, false))
                .build();
    }

}
