package org.example.servlet;

import org.example.dao.*;
import org.example.model.Assessment;
import org.example.model.Course;
import org.example.model.enums.AssessmentType;
import org.example.service.AssessmentService;
import org.example.service.CourseService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AssessmentsServlet", value = "/assessments")
public class AssessmentsServlet extends HttpServlet {
    private AssessmentService assessmentService;
    private CourseService courseService;

    public void init(ServletConfig config) throws ServletException {
        assessmentService = new AssessmentService(new AssessmentDAOImpl());
        courseService = new CourseService(new CourseDAOImpl());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String method = request.getParameter("_method");
            if (method != null) {
                if (method.equals("PUT")) {
                    doPut(request, response);
                    return;
                } else if (method.equals("DELETE")) {
                    doDelete(request, response);
                    return;
                }
            }

            String name = request.getParameter("assessmentName");
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            String type = request.getParameter("assessmentType");
            if(type == null) throw new IllegalArgumentException("Assessment type is required");
            AssessmentType assessmentType = AssessmentType.valueOf(type);
            Course course = courseService.getById(courseId);
            Assessment assessment = new Assessment(course, name, assessmentType);

            assessmentService.create(assessment);

            session.setAttribute("success", "Assessment created successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect("instructor");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int id = Integer.parseInt(request.getParameter("assessmentId"));
            String name = request.getParameter("assessmentName");
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            String type = request.getParameter("assessmentType");
            AssessmentType assessmentType = AssessmentType.valueOf(type);
            if(type == null) throw new IllegalArgumentException("Assessment type is required");
            Course course = courseService.getById(courseId);
            Assessment assessment = new Assessment(id, course, name, assessmentType);
            assessmentService.update(assessment);

            session.setAttribute("success", "Assessment updated successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect("instructor");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int id = Integer.parseInt(request.getParameter("assessmentId"));
            assessmentService.delete(id);

            session.setAttribute("success", "Assessment deleted successfully");
        } catch (IllegalArgumentException e) {

            session.setAttribute("error", e.getMessage());
        }
        response.sendRedirect("instructor");
    }

    public void destroy() {
        assessmentService = null;
        courseService = null;
    }
}