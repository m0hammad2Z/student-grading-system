package org.example.model;

import org.example.model.enums.AssessmentType;

public class Assessment {
    private int id;
    private Course course;
    private String name;
    private AssessmentType type;


    public Assessment() {
    }

    public Assessment(Course course, String name, AssessmentType type) {
        this.course = course;
        this.name = name;
        this.type = type;
    }

    public Assessment(int id, Course course, String name, AssessmentType type) {
        this.id = id;
        this.course = course;
        this.name = name;
        this.type = type;
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

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


    @Override
    public String toString() {
        return "Assessment{" +
                "id=" + id +
                ", course=" + course +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
