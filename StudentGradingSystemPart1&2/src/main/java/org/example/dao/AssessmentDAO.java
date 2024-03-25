package org.example.dao;

import org.example.model.Assessment;

import java.util.List;

public interface AssessmentDAO {
    Assessment createAssessment(Assessment assessment);
    void deleteAssessment(int id);
    Assessment updateAssessment(Assessment assessment);
    Assessment getAssessmentById(int id);
    List<Assessment> getAllAssessments();
    List<Assessment> getAllAssessmentsByCourse(int courseId);
}
