package org.example.service;

import org.example.dao.CourseDAO;
import org.example.dao.RoleDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.model.Course;
import org.example.model.User;
import org.example.util.Validator;

import java.util.List;
import java.util.Map;

public class CourseService  {
    private final CourseDAO courseDAO;
    private final UserService userServices;

    private final Validator validator;

    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
        this.validator = new Validator();
        this.userServices = new UserService(new UserDAOImpl(), new RoleService(new RoleDAOImpl()));
    }


    public Course create(Course course) {
       validator.validObject(course, "Course cannot be null")
                .validName(course.getName(), "Invalid course name")
                .validate();

        Course createdCourse = courseDAO.createCourse(course);
        validator.validObject(createdCourse).validate();
        return createdCourse;
    }


    public void delete(int id) {
        validator.validId(id).validate();

        if (courseDAO.getCourseById(id) == null) {
            throw new IllegalArgumentException("Course not found");
        }

        courseDAO.deleteCourse(id);
    }


    public List<Course> getAll() {
        return courseDAO.getAllCourses();
    }


    public Course getById(int id) {
        validator.validId(id).validate();
        return courseDAO.getCourseById(id);
    }


    public Course update(Course course) {
        validator.validObject(course, "Course cannot be null")
                .validName(course.getName(), "Invalid course name")
                .validate();

        Course updatedCourse = courseDAO.updateCourse(course);
        validator.validObject(updatedCourse).validate();
        return updatedCourse;
    }

    public List<Course> getAllInstructorCourses(int instructorId) {
        validator.validId(instructorId).validate();
        return courseDAO.getAllInstructorCourses(instructorId);
    }

    public List<Course> getAllStudentCourses(int studentId) {
        validator.validId(studentId).validate();
        return courseDAO.getAllStudentCourses(studentId);
    }

    public Map<Course, Double> getCoursesWithAverage(List<Course> courses) {
        validator.validObject(courses).validate();
        List<Integer> coursesIds = new java.util.ArrayList<>();
        for (Course course : courses) {
            coursesIds.add(course.getId());
        }
        return courseDAO.getCoursesWithAverage(coursesIds);
    }

    public void assignInstructor(List<Integer> instructorIds, int courseId) {
        validator.validId(courseId).validate();
        if (instructorIds.isEmpty()) {
            throw new IllegalArgumentException("Instructor list is empty");
        }

        // Check if the course exists and check the instructorIds
        Course course = courseDAO.getCourseById(courseId);

        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        for (int instructorId : instructorIds) {
            User instructor = userServices.getById(instructorId);
            if (instructor == null) {
                throw new IllegalArgumentException("Instructor not found");
            }

            if (courseDAO.isCourseAssigned(instructorId, courseId)) {
                throw new IllegalArgumentException("Instructor is already assigned to the course");
            }

            if (!instructor.getRole().getName().equals("INSTRUCTOR")) {
                throw new IllegalArgumentException("User is not an instructor");
            }
        }


        courseDAO.assignInstructorToCourse(instructorIds, courseId);
    }


    public void enrollStudent(List<Integer> studentIds, int courseId, int instructorId) {
        validator.validId(courseId).
                validId(instructorId).
                validObject(studentIds).
                validate();

        if (studentIds.isEmpty()) {
            throw new IllegalArgumentException("Student list is empty");
        }

        // Check if the course exists, check the instructorId and the studentIds
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        User instructor = userServices.getById(instructorId);
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor not found");
        }
        if (!instructor.getRole().getName().equals("INSTRUCTOR")) {
            throw new IllegalArgumentException("User is not an instructor");
        }

        for (int studentId : studentIds) {
            User student = userServices.getById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found");
            }

            if (courseDAO.isStudentEnrolled(studentId, courseId)) {
                throw new IllegalArgumentException("Student is already enrolled in the course");
            }

            if (!student.getRole().getName().equals("STUDENT")) {
                throw new IllegalArgumentException("User is not a student");
            }
        }

        courseDAO.assignStudentsToCourse(studentIds, courseId, instructorId);
    }

    public void removeInstructor(List<Integer> instructorIds, int courseId) {
        validator.validId(courseId).validObject(instructorIds).validate();

        // Check if the course exists and check the instructorIds
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        for (int instructorId : instructorIds) {
            User instructor = userServices.getById(instructorId);
            if (instructor == null) {
                throw new IllegalArgumentException("Instructor not found");
            }

            if (!instructor.getRole().getName().equals("INSTRUCTOR")) {
                throw new IllegalArgumentException("User is not an instructor");
            }
        }

        courseDAO.removeInstructorFromCourse(instructorIds, courseId);
    }

    public void unrollStudent(List<Integer> studentIds, int courseId, int instructorId) {
        validator.validId(courseId).validObject(studentIds).validate();

        // Check if the course exists and check the studentIds
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        User instructor = userServices.getById(instructorId);
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor not found");
        }

        for (int studentId : studentIds) {
            User student = userServices.getById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found");
            }

            if (!courseDAO.isStudentEnrolled(studentId, courseId, instructorId)) {
                throw new IllegalArgumentException("Student is not enrolled in the course");
            }

            if (!student.getRole().getName().equals("STUDENT")) {
                throw new IllegalArgumentException("User is not a student");
            }
        }

        courseDAO.removeStudentsFromCourse(studentIds, courseId);
    }
}
