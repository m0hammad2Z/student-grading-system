package org.studentgradingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.studentgradingsystem.model.Role;
import org.studentgradingsystem.model.User;
import org.studentgradingsystem.repository.UserProjection;
import org.studentgradingsystem.service.UserService;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errors.append(result.getAllErrors().get(i).getDefaultMessage()).append(", ");
            }
            redirectAttributes.addFlashAttribute("error", errors.toString());
            return "redirect:/admin";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Password cannot be null or empty");
            return "redirect:/admin";
        }
        try {
            User existingUser = userService.getUserByEmail(user.getEmail());
            if (existingUser != null) {
                redirectAttributes.addFlashAttribute("error", "Email already in use");
                return "redirect:/admin";
            }
            userService.addUser(user);
            redirectAttributes.addFlashAttribute("success", "User added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding user");
        }
        return "redirect:/admin";
    }

    @PutMapping("/edit/{id}")
    public String editUser(@PathVariable int id, @Valid @ModelAttribute User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errors.append(result.getAllErrors().get(i).getDefaultMessage()).append(", ");
            }
            redirectAttributes.addFlashAttribute("error", errors.toString());
            return "redirect:/admin";
        }
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/admin";
            }
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user");
        }
        return "redirect:/admin";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/admin";
            }
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user");
        }
        return "redirect:/admin";
    }

    @GetMapping("/instructors")
    public ResponseEntity<List<UserProjection>> getInstructors() {
        try {
            List<UserProjection> instructors = userService.getInstructors();
            return new ResponseEntity<>(instructors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserProjection>> getStudents() {
        try {
            List<UserProjection> students = userService.getStudents();
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}