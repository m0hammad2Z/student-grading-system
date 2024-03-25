package org.example.model;

import org.example.service.AssessmentService;
import org.example.service.CourseService;
import org.example.service.UserService;

public class Course implements java.io.Serializable{
    private int id;
    private String name;

    CourseService courseService;
    AssessmentService assessmentService;
    UserService userService;

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;
        return id == course.id && name.equals(course.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}