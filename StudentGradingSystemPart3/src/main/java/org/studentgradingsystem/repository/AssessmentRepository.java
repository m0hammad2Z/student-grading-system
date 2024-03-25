package org.studentgradingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studentgradingsystem.model.Assessment;
import org.studentgradingsystem.model.Course;
import org.studentgradingsystem.model.Grade;
import org.studentgradingsystem.model.User;

import java.util.List;
import java.util.Map;

public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
    List<Assessment> findByCourseId(int courseId);
}
