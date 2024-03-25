package org.example.servlet;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.example.dao.CourseDAOImpl;
import org.example.dao.RoleDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.model.Course;
import org.example.service.CourseService;
import org.example.service.RoleService;
import org.example.service.UserService;

import java.io.IOException;

@WebServlet(name = "CourseServlet", value = "/courses")
public class CourseServlet extends HttpServlet {
    private CourseService courseService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = new CourseService(new CourseDAOImpl());
        userService = new UserService(new UserDAOImpl(), new RoleService(new RoleDAOImpl()));
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

            String name = request.getParameter("name");
            Course course = new Course(name);

            courseService.create(course);

            session.setAttribute("success", "Course created successfully");

        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }
       response.sendRedirect("admin");
    }

    // Update a course
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            Course course = new Course(id, name);
            courseService.update(course);

            session.setAttribute("success", "Course updated successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect("admin");
    }

    // Delete a course
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            courseService.delete(id);

            session.setAttribute("success", "Course deleted successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }
        response.sendRedirect("admin");
    }

}
