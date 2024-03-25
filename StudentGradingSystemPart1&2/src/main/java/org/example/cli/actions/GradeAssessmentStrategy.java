package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Assessment;
import org.example.model.Grade;
import org.example.model.User;
import org.example.service.GradeService;

public class GradeAssessmentStrategy implements ActionStrategy{
    private GradeService gradeService;
    private User instructor;

    public GradeAssessmentStrategy(GradeService gradeService, User instructor){
        this.gradeService = gradeService;
        this.instructor = instructor;
    }

    @Override
    public Message execute(Message message) {
        int assessmentId;
        int studentId;
        int gradeValue;
        Message.Builder responseBuilder = new Message.Builder();
        try {
            assessmentId = Integer.parseInt((String) message.getData().get("assessment_id").getContent());
            studentId = Integer.parseInt((String) message.getData().get("student_id").getContent());
            gradeValue = Integer.parseInt((String) message.getData().get("grade").getContent());
        } catch (NumberFormatException e) {
            responseBuilder.withData("error", new MessageData("Invalid input", DataMode.OUTPUT, false));
            return responseBuilder.build();
        }
        try {
            Assessment assessment = new Assessment();
            assessment.setId(assessmentId);
            User student = new User();
            student.setId(studentId);
            Grade grade = new Grade(assessment, student, gradeValue);
            gradeService.create(grade);
            responseBuilder.withData("success", new MessageData("Assessment graded successfully", DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to grade assessment", DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message getPreparedData() {
        return new Message.Builder()
                .withData("assessment_id", new MessageData("Enter assessment ID", DataMode.INPUT, true))
                .withData("student_id", new MessageData("Enter student ID", DataMode.INPUT, true))
                .withData("grade", new MessageData("Enter grade", DataMode.INPUT, true))
                .build();
    }
}