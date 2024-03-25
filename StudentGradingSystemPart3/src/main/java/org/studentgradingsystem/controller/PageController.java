package org.studentgradingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.studentgradingsystem.model.*;
import org.studentgradingsystem.service.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RoleService rolesService;


    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/admin")
    public String admin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = null;
        try {
            user = userService.getUserByEmail(userDetails.getUsername());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        // Ensure user object is always added to the model
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", rolesService.getAllRoles());
        model.addAttribute("courses", courseService.getAllCourses());

        return "admin";
    }

    // Instructor have the course he is teaching, the assessments of the course and the students in the course
    @GetMapping("/instructor")
    public String instructor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = null;
        try {
            user = userService.getUserByEmail(userDetails.getUsername());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        model.addAttribute("user", user);
        List<Course> courses = courseService.getCoursesByInstructorId(user.getId());

        for (Course course : courses) {
            if (course == null || course.getAssessments() == null) {
                model.addAttribute("error", "Course or assessments not found");
                return "error";
            }
        }

        model.addAttribute("courses", courses);

        return "instructor";
    }


    @GetMapping("/student")
    public String student(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = null;
        try {
            user = userService.getUserByEmail(userDetails.getUsername());
            model.addAttribute("user", user);

            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(user.getId());

            Map <Enrollment, Double> averageGrades = new java.util.HashMap<>();
            for (Enrollment enrollment : enrollments) {
                averageGrades.put(enrollment, gradeService.getAverageGradeByCourseId(enrollment.getCourse().getId()));
            }

            model.addAttribute("averageGrades", averageGrades);

            List <Grade> grades = gradeService.getGradesByStudentId(user.getId());
            model.addAttribute("grades", grades);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "student";
    }

    @GetMapping("/grade/{assessmentId}/{courseId}")
    public String grading(Model model, @PathVariable("assessmentId") int assessmentId, @PathVariable("courseId") int courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = null;
        user = userService.getUserByEmail(userDetails.getUsername());

        // Get all the enrollments of the instructor for a assessment, and then get the grades of the students
        Course course = courseService.getCourseById(courseId);
        Assessment assessment = assessmentService.getAssessmentById(assessmentId);

        Map<User, Grade> grades = new java.util.HashMap<>();

        for (Enrollment enrollment : course.getEnrollments()) {
            User student = enrollment.getStudent();

            if(user.getId() != enrollment.getInstructor().getId()){
                continue;
            }


            Grade grade = gradeService.getGradeByStudentAndAssessment(student, assessment);
            if (grade != null) {
                grades.put(student, grade);
            }else{
                grades.put(student, new Grade( 0, assessment, student));
            }
        }

        model.addAttribute("grades", grades);
        model.addAttribute("assessmentName", assessment.getName());
        model.addAttribute("assessmentType", assessment.getAssessmentType());
        model.addAttribute("assessmentId", assessment.getId());
        model.addAttribute("courseName", course.getName());
        model.addAttribute("user", user);
        return "grading";
    }
}
