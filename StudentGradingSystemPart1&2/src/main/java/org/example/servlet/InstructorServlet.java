package org.example.servlet;

import org.example.dao.*;
import org.example.model.*;
import org.example.model.enums.AssessmentType;
import org.example.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@javax.servlet.annotation.WebServlet(name = "InstructorServlet", value = "/instructor")
public class InstructorServlet extends HttpServlet {
    private CourseService courseService;
    private AssessmentService assessmentService;

    @Override
    public void init() {
        courseService = new CourseService(new CourseDAOImpl());
        assessmentService = new AssessmentService(new AssessmentDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User instructor = (User) request.getSession().getAttribute("user");

        if (!instructor.getRole().getName().equals("INSTRUCTOR")) {
            response.setStatus(403);
            return;
        }

        // Retrieve the data
        List<Course> courses = courseService.getAllInstructorCourses(instructor.getId());

        AssessmentType[] assessmentTypes = AssessmentType.values();

        // Get the assessments for each course
        List<Assessment> assessments = new ArrayList<>();
        for (Course course : courses) {
            assessments.addAll(assessmentService.getAssessmentsByCourseId(course.getId()));
        }

        request.setAttribute("courses", courses);
        request.setAttribute("assessments", assessments);
        request.setAttribute("instructor", instructor);
        request.setAttribute("assessmentTypes", assessmentTypes);

        request.getRequestDispatcher("instructor.jsp").forward(request, response);
    }
}