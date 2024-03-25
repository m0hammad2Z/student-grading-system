package org.studentgradingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Min(value = 0, message = "Score cannot be less than 0")
    @Max(value = 100, message = "Score cannot be greater than 100")
    private int grade;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    @NotNull(message = "Assessment cannot be null")
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student cannot be null")
    private User student;

    public Grade() {
    }

    public Grade(int grade, Assessment assessment, User student) {
        this.grade = grade;
        this.assessment = assessment;
        this.student = student;
    }

    public Grade(int id, int grade, Assessment assessment, User student) {
        this.id = id;
        this.grade = grade;
        this.assessment = assessment;
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}