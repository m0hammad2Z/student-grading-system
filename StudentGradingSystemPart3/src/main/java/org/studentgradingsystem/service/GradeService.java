package org.studentgradingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.studentgradingsystem.model.Assessment;
import org.studentgradingsystem.model.Grade;
import org.studentgradingsystem.model.User;
import org.studentgradingsystem.repository.GradeRepository;

import java.util.List;

@Service
public class GradeService {
    @Autowired
    private GradeRepository gradeRepository;

    public Grade getGradeByStudentAndAssessment(User student, Assessment assessment) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        if (assessment == null) {
            throw new IllegalArgumentException("Assessment cannot be null");
        }

        return gradeRepository.findByStudentAndAssessment(student, assessment);
    }


    // Update or add a grade for a student if it does not exist
    public Grade updateOrAddGrade(int grade, int studentId, int assessmentId) {

        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }

        User student = new User();
        student.setId(studentId);

        Assessment assessment = new Assessment();
        assessment.setId(assessmentId);


        Grade gradeObj = gradeRepository.findByStudentAndAssessment(student, assessment);
        if (gradeObj == null) {
            gradeObj = new Grade(grade, assessment, student);
            return gradeRepository.save(gradeObj);
        } else {
            gradeObj.setGrade(grade);
            return gradeRepository.save(gradeObj);
        }
    }

    public List<Grade> getGradesByStudentId(int id) {
        return gradeRepository.findByStudentId(id);
    }


    public double getAverageGradeByCourseId(int courseId) {
        Double average = gradeRepository.findAverageGradeByCourseId(courseId);
        if (average == null) {
            return 0;
        }
        return average;
    }
}