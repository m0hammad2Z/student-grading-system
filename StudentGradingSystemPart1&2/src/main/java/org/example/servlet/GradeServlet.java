package org.example.servlet;

import org.example.dao.*;

import org.example.model.Assessment;
import org.example.model.Course;
import org.example.model.Grade;
import org.example.model.User;
import org.example.service.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@javax.servlet.annotation.WebServlet(name = "GradeServlet", value = "/grades")
public class GradeServlet extends HttpServlet {
    private GradeService gradeService;
    private UserService userService;
    private AssessmentService assessmentService;
    private CourseService courseService;

    public void init(ServletConfig config) throws ServletException {
        gradeService = new GradeService(new GradeDAOImpl());
        userService = new UserService(new UserDAOImpl(), new RoleService(new RoleDAOImpl()));
        assessmentService = new AssessmentService(new AssessmentDAOImpl());
        courseService = new CourseService(new CourseDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int assessmentId = Integer.parseInt(request.getParameter("assessmentId"));
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.setStatus(401);
                return;
            }

            List<Grade> grades = gradeService.getAllByAssessment(assessmentId);
            request.setAttribute("grades", grades);

            Course course = courseService.getById(courseId);
            request.setAttribute("course", course);

            Assessment assessment = assessmentService.getById(assessmentId);
            request.setAttribute("assessment", assessment);

            request.getRequestDispatcher("grades.jsp").forward(request, response);
        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int assessmentId = Integer.parseInt(request.getParameter("assessmentId"));
        int grade = Integer.parseInt(request.getParameter("grade"));
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        if(grade < 0 || grade > 100) {
            request.getSession().setAttribute("error", "Grade must be between 0 and 100");
            response.sendRedirect("grades?courseId=" + courseId + "&assessmentId=" + assessmentId);
            return;
        }

        try {
            // Try to get a grade that already exists for the student and assessment
            Grade existingGrade = gradeService.getByStudentAndAssessment(studentId, assessmentId);

            // If the grade already exists, update it
            if (existingGrade != null) {
                existingGrade.setGrade(grade);
                gradeService.update(existingGrade);
                request.getSession().setAttribute("success", "Grade updated successfully");
            } else {
                // If the grade does not exist, create a new one
                User user = userService.getById(studentId);
                Assessment assessment = assessmentService.getById(assessmentId);

                Grade newGrade = new Grade(assessment, user, grade);
                gradeService.create(newGrade);
                request.getSession().setAttribute("success", "Grade added successfully");
            }
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        response.sendRedirect("grades?courseId=" + courseId + "&assessmentId=" + assessmentId);
    }
}
