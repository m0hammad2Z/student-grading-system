package org.studentgradingsystem.repository;

import org.studentgradingsystem.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    Enrollment findByStudentIdAndCourseIdAndInstructorId(int studentId, int courseId, int instructorId);

    List<Enrollment> findByStudentId(int id);
}