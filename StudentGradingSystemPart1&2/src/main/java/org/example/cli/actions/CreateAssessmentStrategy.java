package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Assessment;
import org.example.model.Course;
import org.example.model.enums.AssessmentType;
import org.example.service.AssessmentService;

public class CreateAssessmentStrategy implements ActionStrategy{
    private AssessmentService assessmentService;

    public CreateAssessmentStrategy(AssessmentService assessmentService){
        this.assessmentService = assessmentService;
    }

    @Override
    public Message execute(Message message) {
        int courseId;
        String name;
        String type;
        Message.Builder responseBuilder = new Message.Builder();
        try {
            courseId = Integer.parseInt((String) message.getData().get("course_id").getContent());
            name = (String) message.getData().get("name").getContent();
            type = (String) message.getData().get("type").getContent();
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid course ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        try {
            Course course = new Course();
            course.setId(courseId);
            Assessment assessment = new Assessment(course, name, AssessmentType.valueOf(type));
            assessmentService.create(assessment);
            responseBuilder.withData("success", new MessageData("Assessment created successfully", DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to create assessment", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("course_id", new MessageData("Enter course ID", DataMode.INPUT, true))
                .withData("name", new MessageData("Enter assessment name", DataMode.INPUT, true))
                .withData("type", new MessageData("Enter assessment type", DataMode.INPUT, true))
                .build();
    }
}