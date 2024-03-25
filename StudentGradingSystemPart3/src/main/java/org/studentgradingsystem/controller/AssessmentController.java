package org.studentgradingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.studentgradingsystem.model.Assessment;
import org.studentgradingsystem.model.Course;
import org.studentgradingsystem.model.User;
import org.studentgradingsystem.service.AssessmentService;
import org.studentgradingsystem.service.CourseService;
import org.studentgradingsystem.service.RoleService;
import org.studentgradingsystem.service.UserService;
import org.studentgradingsystem.model.AssessmentType;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
@RequestMapping("/assessment")
public class AssessmentController {

    @Autowired
    private UserService userService;

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RoleService rolesService;

    // Initializes data binding and registers custom editors for the AssessmentType enum and String
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(AssessmentType.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(AssessmentType.valueOf(text.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    setValue(null);
                    binder.getBindingResult().rejectValue("assessmentType", "error.assessment", "Invalid value for AssessmentType.");
                }
            }
        });

        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    @PostMapping("/add")
    public String addAssessment(@Valid @ModelAttribute Assessment assessment, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errors.append(result.getAllErrors().get(i).getDefaultMessage()).append(", ");
            }
            redirectAttributes.addFlashAttribute("error", errors.toString());
            return "redirect:/instructor";
        }
        try {
            assessmentService.addAssessment(assessment);
            redirectAttributes.addFlashAttribute("success", "Assessment added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding assessment");
        }
        return "redirect:/instructor";
    }

    @PutMapping("/edit/{id}")
    public String editAssessment(@PathVariable int id, @Valid @ModelAttribute Assessment assessment, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errors.append(result.getAllErrors().get(i).getDefaultMessage()).append(", ");
            }
            redirectAttributes.addFlashAttribute("error", errors.toString());
            return "redirect:/instructor";
        }
        try {
            assessmentService.updateAssessment(assessment);
            redirectAttributes.addFlashAttribute("success", "Assessment edited successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error editing assessment");
        }
        return "redirect:/instructor";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteAssessment(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            assessmentService.deleteAssessment(id);
            redirectAttributes.addFlashAttribute("success", "Assessment deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting assessment");
        }
        return "redirect:/instructor";
    }


}