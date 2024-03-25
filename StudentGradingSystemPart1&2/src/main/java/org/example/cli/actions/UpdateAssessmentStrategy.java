package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Assessment;
import org.example.model.enums.AssessmentType;
import org.example.service.AssessmentService;

public class UpdateAssessmentStrategy implements ActionStrategy{
    private AssessmentService assessmentService;

    public UpdateAssessmentStrategy(AssessmentService assessmentService){
        this.assessmentService = assessmentService;
    }

    @Override
    public Message execute(Message message) {
        int assessmentId;
        String name;
        String type;
        Message.Builder responseBuilder = new Message.Builder();
        try {
            assessmentId = Integer.parseInt((String) message.getData().get("assessment_id").getContent());
            name = (String) message.getData().get("name").getContent();
            type = (String) message.getData().get("type").getContent();
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid assessment ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        try {
            Assessment assessment = assessmentService.getById(assessmentId);
            if (assessment == null) {
                responseBuilder.withData("error", new MessageData("Assessment not found", DataMode.OUTPUT, false));
                return responseBuilder.build();
            }
            assessment.setName(name);
            assessment.setType(AssessmentType.valueOf(type));
            assessmentService.update(assessment);
            responseBuilder.withData("success", new MessageData("Assessment updated successfully", DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to update assessment", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("assessment_id", new MessageData("Enter assessment ID", DataMode.INPUT, true))
                .withData("name", new MessageData("Enter new assessment name", DataMode.INPUT, true))
                .withData("type", new MessageData("Enter new assessment type", DataMode.INPUT, true))
                .build();
    }
}