package org.studentgradingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.studentgradingsystem.model.Course;

import org.studentgradingsystem.model.Enrollment;
import org.studentgradingsystem.model.User;
import org.studentgradingsystem.service.CourseService;

import org.studentgradingsystem.service.UserService;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errors.append(result.getAllErrors().get(i).getDefaultMessage()).append(", ");
            }
            redirectAttributes.addFlashAttribute("error", errors.toString());
            return "redirect:/admin";
        }
        try {
            courseService.addCourse(course);
            redirectAttributes.addFlashAttribute("success", "Course added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding course");
        }
        return "redirect:/admin";
    }

    @PutMapping("/edit/{id}")
    public String editCourse(@PathVariable int id, @Valid @ModelAttribute Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errors.append(result.getAllErrors().get(i).getDefaultMessage()).append(", ");
            }
            redirectAttributes.addFlashAttribute("error", errors.toString());
            return "redirect:/admin";
        }
        if (course.getId() != id) {
            redirectAttributes.addFlashAttribute("error", "Course id mismatch");
            return "redirect:/admin";
        }

        try {
            Course existingCourse = courseService.getCourseById(id);
            if (existingCourse == null) {
                redirectAttributes.addFlashAttribute("error", "Course not found");
                return "redirect:/admin";
            }
            courseService.updateCourse(course);
            redirectAttributes.addFlashAttribute("success", "Course updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating course");
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCourse(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (id <= 0) {
            redirectAttributes.addFlashAttribute("error", "Invalid course id");
            return "redirect:/admin";
        }
        try {
            Course existingCourse = courseService.getCourseById(id);
            if (existingCourse == null) {
                redirectAttributes.addFlashAttribute("error", "Course not found");
                return "redirect:/admin";
            }
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting course");
        }
        return "redirect:/admin";
    }

    @PostMapping("/assignInstructors/{courseId}")
    public String assignInstructors(@PathVariable int courseId, @RequestParam(value = "instructorIds[]", required = false) List<Integer> instructorIds, RedirectAttributes redirectAttributes) {
        try {
            courseService.assignInstructors(courseId, instructorIds);
            redirectAttributes.addFlashAttribute("success", "Instructors assigned successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error assigning instructors");
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}/instructors")
    public ResponseEntity<List<User>> getInstructors(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<User> instructors = course.getInstructors();
        return new ResponseEntity<>(instructors, HttpStatus.OK);
    }

    @PostMapping("/enroll/{courseId}")
    public String enrollStudents(@PathVariable int courseId, @RequestParam(value = "studentIds[]", required = false) List<Integer> studentIds, RedirectAttributes redirectAttributes) {
        // Get the current user from the security context
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        try {
            courseService.assignStudents(courseId, studentIds, user.getId());
            redirectAttributes.addFlashAttribute("success", "Students assigned successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error assigning students" + e.getMessage());
        }
        return "redirect:/instructor";
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<User>> getStudents(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Enrollment> enrollments = course.getEnrollments();
        List<User> students = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            students.add(enrollment.getStudent());
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

}
