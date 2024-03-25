package org.example.servlet;

import org.example.dao.*;
import org.example.service.*;
import org.example.model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private UserService userService;
    private RoleService roleService;
    private CourseService courseService;

    @Override
    public void init() {
        roleService = new RoleService(new RoleDAOImpl());
        userService = new UserService(new UserDAOImpl(), roleService);
        courseService = new CourseService(new CourseDAOImpl());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.getAll();
        List<Course> courses = courseService.getAll();
        List<Role> roles = roleService.getAll();
        request.setAttribute("users", users);
        request.setAttribute("courses", courses);
        request.setAttribute("roles", roles);

        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }


}
