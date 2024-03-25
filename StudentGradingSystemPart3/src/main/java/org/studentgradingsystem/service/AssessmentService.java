package org.studentgradingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentgradingsystem.model.*;
import org.studentgradingsystem.repository.AssessmentRepository;

import java.util.List;
import java.util.Map;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CourseService courseService;

    public List<Assessment> getAllAssessments() {
        List<Assessment> assessments = assessmentRepository.findAll();
        if (assessments.isEmpty()) {
            throw new IllegalArgumentException("No assessments found");
        }
        return assessments;
    }

    public Assessment getAssessmentById(int id) {
        Assessment assessment = assessmentRepository.findById(id).orElse(null);
        if (assessment == null) {
            throw new IllegalArgumentException("Assessment not found");
        }
        return assessment;
    }

    public Assessment addAssessment(Assessment assessment) {
        if (assessment == null) {
            throw new IllegalArgumentException("Assessment cannot be null");
        }
        if (assessment.getName() == null) {
            throw new IllegalArgumentException("Assessment name cannot be null");
        }
        if (assessment.getAssessmentType() == null) {
            throw new IllegalArgumentException("Assessment type cannot be null");
        }

        return assessmentRepository.save(assessment);
    }

    public Assessment updateAssessment(Assessment assessment) {
        if (assessmentRepository.findById(assessment.getId()).orElse(null) == null) {
            throw new IllegalArgumentException("Assessment not found");
        }

        return assessmentRepository.save(assessment);
    }

    public void deleteAssessment(int id) {
        assessmentRepository.deleteById(id);
    }

    public List<Assessment> getAssessmentsByCourseId(int courseId) {
        if (courseService.getCourseById(courseId) == null) {
            throw new IllegalArgumentException("Course not found");
        }

        return assessmentRepository.findByCourseId(courseId);

    }
}