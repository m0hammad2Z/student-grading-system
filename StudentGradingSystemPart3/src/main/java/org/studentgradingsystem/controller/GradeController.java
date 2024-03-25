package org.studentgradingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.studentgradingsystem.model.Assessment;
import org.studentgradingsystem.model.Grade;
import org.studentgradingsystem.service.AssessmentService;
import org.studentgradingsystem.service.EnrollmentService;
import org.studentgradingsystem.service.GradeService;
import org.studentgradingsystem.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AssessmentService assessmentService;


    @PostMapping("/grading/{studentId}/{assessmentId}")
    public String gradeStudent(@PathVariable int studentId, @PathVariable int assessmentId, @RequestParam("grade") int grade, RedirectAttributes redirectAttributes) {
        Assessment assessment = null;
        try {
            assessment = assessmentService.getAssessmentById(assessmentId);
            enrollmentService.checkEnrollment(studentId, assessmentService.getAssessmentById(assessmentId).getCourse().getId(), userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
            gradeService.updateOrAddGrade(grade, studentId, assessmentId);
            redirectAttributes.addFlashAttribute("success", "Grade added successfully");
        }
        catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/instructor/";
        }
        return "redirect:/grade/" + assessmentId + "/" + assessment.getCourse().getId();
    }

}
