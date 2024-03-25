package org.studentgradingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "assessments")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Assessment name cannot be null")
    @NotBlank(message = "Assessment name cannot be blank")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull(message = "Assessment type cannot be null")
    private AssessmentType assessmentType;


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Course cannot be null")
    private Course course;

    @OneToMany(mappedBy = "assessment")
    private Set<Grade> grades;

    public Assessment() {
    }

    public Assessment(String name, AssessmentType assessmentType, Course course) {
        this.name = name;
        this.assessmentType = assessmentType;
        this.course = course;
    }

    public Assessment(int id, String name, AssessmentType assessmentType, Course course) {
        this.id = id;
        this.name = name;
        this.assessmentType = assessmentType;
        this.course = course;
    }

    public Assessment(int id, String name, AssessmentType assessmentType, Course course, Set<Grade> grades) {
        this.id = id;
        this.name = name;
        this.assessmentType = assessmentType;
        this.course = course;
        this.grades = grades;
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

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }
}