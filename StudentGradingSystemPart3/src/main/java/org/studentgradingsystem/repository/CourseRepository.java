package org.studentgradingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studentgradingsystem.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByInstructorsId(int instructorId);
}
