package org.studentgradingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.studentgradingsystem.model.Assessment;
import org.studentgradingsystem.model.Grade;
import org.studentgradingsystem.model.User;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Grade findByStudentAndAssessment(User student, Assessment assessment);

    List<Grade> findByStudentId(int id);

    @Query("Select avg(g.grade) from Grade g where g.assessment.course.id = ?1")
    Double findAverageGradeByCourseId(int courseId);
}
