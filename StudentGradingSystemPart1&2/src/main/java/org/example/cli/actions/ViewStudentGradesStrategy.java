package org.example.cli.actions;

import org.example.cli.util.DataMode;
import org.example.cli.util.Message;
import org.example.cli.util.MessageData;
import org.example.model.Grade;
import org.example.model.User;
import org.example.service.GradeService;

import java.util.List;

public class ViewStudentGradesStrategy implements ActionStrategy{
    private GradeService gradeService;
    private final User student;

    public ViewStudentGradesStrategy(GradeService gradeService, User student){
        this.gradeService = gradeService;
        this.student = student;
    }

    @Override
    public Message getPreparedData() {
        Message.Builder responseBuilder = new Message.Builder();
        try {
            StringBuilder grades = new StringBuilder();
            List<Grade> studentGrades = gradeService.getAllByStudent(student.getId());
            responseBuilder.withData("grades", new MessageData(grades.toString(), DataMode.OUTPUT, false));
        } catch (IllegalArgumentException e) {
            responseBuilder.withData("error", new MessageData(e.getMessage(), DataMode.OUTPUT, false));
        } catch (Exception e) {
            responseBuilder.withData("error", new MessageData("Failed to retrieve grades" + e.getMessage(), DataMode.OUTPUT, false));
        }
        return responseBuilder.build();
    }

    @Override
    public Message execute(Message message) {
        return new Message.Builder()
                .withData("empty", new MessageData("", DataMode.OUTPUT, false))
                .build();
    }
}