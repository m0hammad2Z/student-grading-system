package org.example.model;

import org.example.service.AssessmentService;
import org.example.service.UserService;

public class Grade {

    private int id;
    private Assessment assessment;
    public User student;
    private int grade;

    public Grade() {
    }

    public Grade(Assessment assessment, User student, int grade) {
        this.assessment = assessment;
        this.student = student;
        this.grade = grade;
    }

    public Grade(int id, Assessment assessment, User student, int grade) {
        this.id = id;
        this.assessment = assessment;
        this.student = student;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", assessment=" + assessment.getName() +
                ", student=" + student.getFirstName() + " " + student.getLastName() +
                ", grade=" + grade +
                '}';
    }
}
