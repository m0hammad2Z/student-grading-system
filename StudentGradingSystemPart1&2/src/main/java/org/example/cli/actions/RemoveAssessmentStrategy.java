package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.service.AssessmentService;

public class RemoveAssessmentStrategy implements ActionStrategy{
    private AssessmentService assessmentService;

    public RemoveAssessmentStrategy(AssessmentService assessmentService){
        this.assessmentService = assessmentService;
    }

    @Override
    public Message execute(Message message) {
        int assessmentId;
        Message.Builder responseBuilder = new Message.Builder();
        try {
            assessmentId = Integer.parseInt((String) message.getData().get("assessment_id").getContent());
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid assessment ID", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        try {
            assessmentService.delete(assessmentId);
            responseBuilder.withData("success", new MessageData("Assessment removed successfully", DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to remove assessment", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("assessment_id", new MessageData("Enter assessment ID", DataMode.INPUT, true))
                .build();
    }
}