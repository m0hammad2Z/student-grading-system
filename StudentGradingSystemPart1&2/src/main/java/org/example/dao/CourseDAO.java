package org.example.dao;

import org.example.model.Course;

import java.util.List;
import java.util.Map;

public interface CourseDAO {
    Course createCourse(Course course);
    void deleteCourse(int id);
    List<Course> getAllCourses();
    Course getCourseById(int id);
    List<Course> getAllInstructorCourses(int instructorId);
    List<Course> getAllUnassignedCourses(int instructorId);
    List<Course> getAllStudentCourses(int studentId);
    // Get average in a list of courses
    Map<Course, Double> getCoursesWithAverage(List<Integer> courseIds);
    Course updateCourse(Course course);
    boolean assignInstructorToCourse(List<Integer> instructorIds, int courseId);
    boolean removeInstructorFromCourse(List<Integer> instructorIds, int courseId);
    boolean assignStudentsToCourse(List<Integer> studentIds, int courseId, int instructorId);
    boolean removeStudentsFromCourse(List<Integer> studentIds, int courseId);

    boolean isStudentEnrolled(int studentId, int courseId, int instructorId);

    boolean isStudentEnrolled(int studentId, int courseId);

    boolean isCourseAssigned(int courseId, int instructorId);
}
