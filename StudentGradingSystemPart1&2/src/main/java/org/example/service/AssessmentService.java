package org.example.service;

import org.example.dao.AssessmentDAO;
import org.example.model.Assessment;
import org.example.util.Validator;

import java.util.List;

public class AssessmentService {
    private final AssessmentDAO assessmentDAO;
    private final Validator validator;

    public AssessmentService(AssessmentDAO assessmentDAO) {
        this.assessmentDAO = assessmentDAO;
        this.validator = new Validator();
    }

    public Assessment create(Assessment assessment) {
        validator.validObject(assessment)
                .validName(assessment.getName())
                .validEnum(assessment.getType(), "Assessment type is required")
                .validId(assessment.getCourse().getId())
                .validate();

        Assessment assessment1 = assessmentDAO.createAssessment(assessment);
        validator.validObject(assessment1).validate();
        return assessment1;
    }

    public void delete(int id) {
        validator.validId(id).validate();

        if (assessmentDAO.getAssessmentById(id) == null) {
            throw new IllegalArgumentException("Assessment not found");
        }

        assessmentDAO.deleteAssessment(id);
    }
    public List<Assessment> getAll() {
        return assessmentDAO.getAllAssessments();
    }

    public Assessment getById(int id) {
        validator.validId(id).validate();
        return assessmentDAO.getAssessmentById(id);
    }

    public Assessment update(Assessment assessment) {
        validator.validObject(assessment)
                .validName(assessment.getName())
                .validEnum(assessment.getType(), "Assessment type is required")
                .validId(assessment.getCourse().getId())
                .validate();
        Assessment assessment1 = assessmentDAO.updateAssessment(assessment);
        validator.validObject(assessment1).validate();
        return assessment1;
    }

    public List<Assessment> getAssessmentsByCourseId(int courseId) {
        validator.validId(courseId).validate();
        return assessmentDAO.getAllAssessmentsByCourse(courseId);
    }
}
