package org.studentgradingsystem.service;

import org.studentgradingsystem.model.Enrollment;
import org.studentgradingsystem.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;



    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        if (enrollments.isEmpty()) {
            throw new IllegalArgumentException("No enrollments found");
        }
        return enrollments;
    }

    public Enrollment getEnrollmentById(int id) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment not found");
        }
            return enrollment;
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }

        if (enrollment.getCourse() == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        if (enrollment.getStudent() == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        if (enrollment.getInstructor() == null) {
            throw new IllegalArgumentException("Instructor cannot be null");
        }

        return enrollmentRepository.save(enrollment);
    }

    public void checkEnrollment(int studentId, int courseId, int instructorId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseIdAndInstructorId(studentId, courseId, instructorId);
        if (enrollment == null) {
            throw new IllegalArgumentException("Student is not enrolled in the course");
        }
    }

    public List<Enrollment> getEnrollmentsByStudentId(int id) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(id);
        if (enrollments.isEmpty()) {
            throw new IllegalArgumentException("No enrollments found");
        }
        return enrollments;
    }

    public void deleteEnrollment(int id) {
        enrollmentRepository.deleteById(id);
    }
}