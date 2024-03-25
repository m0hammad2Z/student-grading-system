package org.example.service;

import org.example.dao.GradeDAO;
import org.example.model.Grade;
import org.example.util.Validator;

import java.util.List;

public class GradeService {
    private final GradeDAO gradeDAO;

    Validator validator;

    public GradeService(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
        this.validator = new Validator();
    }


    public Grade create(Grade grade) {
        validator.validObject(grade)
                .validId(grade.getStudent().getId())
                .validId(grade.getAssessment().getId())
                .validNumber(grade.getGrade(),0,100)
                .validate();
        Grade createdGrade = gradeDAO.createGrade(grade);
        validator.validObject(createdGrade).validate();
        return createdGrade;
    }


    public void delete(int id) {
        validator.validId(id).validate();
        gradeDAO.deleteGrade(id);
    }


    public List<Grade> getAll() {
        return gradeDAO.getAllGrades();
    }
    public Grade getById(int id) {
        validator.validId(id).validate();
        Grade grade = gradeDAO.getGradeById(id);
        validator.validObject(grade).validate();
        return grade;
    }

    public List<Grade> getAllByStudent(int studentId) {
        validator.validId(studentId).validate();
        return gradeDAO.getAllGradesByStudent(studentId);
    }

    public Grade update(Grade grade) {
        validator.validObject(grade)
                .validId(grade.getStudent().getId())
                .validId(grade.getAssessment().getId())
                .validNumber(grade.getGrade(),0,100)
                .validate();
        Grade updatedGrade = gradeDAO.updateGrade(grade);
        validator.validObject(updatedGrade).validate();
        return updatedGrade;
    }

    public List<Grade> getAllByAssessment(int assessmentId) {
        validator.validId(assessmentId).validate();
        return gradeDAO.getAssessmentGradesForAllStudents(assessmentId);
    }

    public Grade getByStudentAndAssessment(int studentId, int assessmentId) {
        validator.validId(studentId)
                .validId(assessmentId)
                .validate();
        return gradeDAO.getGradeByStudentAndAssessment(studentId, assessmentId);
    }
}
