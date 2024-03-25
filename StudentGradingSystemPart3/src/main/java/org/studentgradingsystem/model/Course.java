package org.studentgradingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Course name cannot be blank")
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Assessment> assessments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_instructors",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id"))
    private List<User> instructors = new ArrayList<>();


    @OneToMany(mappedBy = "course" , fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    public List<User> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<User> instructors) {
        this.instructors = instructors;
    }

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public Course(int id, String name, List<Assessment> assessments) {
        this.id = id;
        this.name = name;
        this.assessments = assessments;
    }

    public Course(int id, String name, List<Assessment> assessments, List<Enrollment> enrollments) {
        this.id = id;
        this.name = name;
        this.assessments = assessments;
        this.enrollments = enrollments;
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

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

}