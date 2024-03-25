package org.example.dao;

import org.example.model.Grade;

import java.util.List;

public interface GradeDAO {
    Grade createGrade(Grade grade);
    Grade updateGrade(Grade grade);
    void deleteGrade(int id);
    Grade getGradeById(int id);
    List<Grade> getAllGrades();
    List<Grade> getAllGradesByStudent(int studentId);
    List<Grade> getAssessmentGradesForAllStudents(int assessmentId);
    Grade getGradeByStudentAndAssessment(int studentId, int assessmentId);
}
