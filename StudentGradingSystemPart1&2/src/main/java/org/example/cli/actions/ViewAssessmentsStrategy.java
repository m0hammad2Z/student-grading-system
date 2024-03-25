package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.service.AssessmentService;

public class ViewAssessmentsStrategy implements ActionStrategy{
    private AssessmentService assessmentService;

    public ViewAssessmentsStrategy(AssessmentService assessmentService){
        this.assessmentService = assessmentService;
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
            StringBuilder assessments = new StringBuilder();
            assessmentService.getAll().forEach(assessment -> assessments.append(assessment).append("\n"));
            responseBuilder.withData("assessments", new MessageData(assessments.toString(), DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to retrieve assessments", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }
}