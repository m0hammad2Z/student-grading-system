package org.studentgradingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull(message = "Course cannot be null")
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @NotNull(message = "Student cannot be null")
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    public Enrollment() {
    }

    public Enrollment(Course course, User student, User instructor) {
        this.course = course;
        this.student = student;
        this.instructor = instructor;
    }

    public Enrollment(int id, Course course, User student, User instructor) {
        this.id = id;
        this.course = course;
        this.student = student;
        this.instructor = instructor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }
}
