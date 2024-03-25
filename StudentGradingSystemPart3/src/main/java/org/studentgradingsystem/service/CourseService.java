package org.studentgradingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentgradingsystem.model.Course;
import org.studentgradingsystem.model.Enrollment;
import org.studentgradingsystem.model.User;
import org.studentgradingsystem.repository.CourseRepository;

import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            throw new IllegalArgumentException("No courses found");
        }
        return courses;
    }

    public Course getCourseById(int id) {
        Course course = courseRepository.findById(id).orElse(null);

        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        return course;
    }

    public Course addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }



        return courseRepository.save(course);
    }

    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    public void assignInstructors(int courseId, List<Integer> instructorIds) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        if(instructorIds == null || instructorIds.isEmpty()) {
            throw new IllegalArgumentException("Instructor ids cannot be null or empty");
        }

        List<User> instructors = userService.getUsersByIds(instructorIds);
        course.setInstructors(instructors);
        courseRepository.save(course);
    }

    public List<Course> getCoursesByInstructorId(int instructorId) {
        return courseRepository.findByInstructorsId(instructorId);
    }

    // Assign students to a course and remove any previous enrollments
    public void assignStudents(int courseId, List<Integer> studentIds, int instructorId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        if (studentIds == null || studentIds.isEmpty()) {
            throw new IllegalArgumentException("Student ids cannot be null or empty");
        }

        User instructor = userService.getUserById(instructorId);
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor not found");
        }

        Course course1 = courseRepository.findById(courseId).orElse(null);
        if (course1 == null) {
            throw new IllegalArgumentException("Course not found");
        }

        // Remove previous enrollments
        List<Enrollment> enrollments = course1.getEnrollments();
        for (Enrollment enrollment : enrollments) {
            enrollmentService.deleteEnrollment(enrollment.getId());
        }

        course1.getEnrollments().clear();
        courseRepository.save(course1);

        for (int studentId : studentIds) {
            User student = userService.getUserById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found");
            }

            Enrollment enrollment = new Enrollment(course,student, instructor);
            enrollmentService.createEnrollment(enrollment);
        }
    }


}