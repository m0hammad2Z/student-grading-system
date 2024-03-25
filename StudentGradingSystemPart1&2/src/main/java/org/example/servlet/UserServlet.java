package org.example.servlet;

import org.example.dao.RoleDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.model.Role;
import org.example.model.User;
import org.example.service.RoleService;
import org.example.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {
    private UserService userService;
    private RoleService roleService;

    @Override
    public void init() {
        roleService = new RoleService(new RoleDAOImpl());
        userService = new UserService(new UserDAOImpl(), roleService);
    }

    // Create a new user
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        HttpSession session = request.getSession();
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            int roleId = Integer.parseInt(request.getParameter("role"));

            Role role = roleService.getById(roleId);

            User user = new User(firstName, lastName, email, password);
            user.setRole(role);
            userService.create(user);

            session.setAttribute("success", "User created successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }
        response.sendRedirect("admin");
    }

    // Update a user
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String id = request.getParameter("id");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            int roleId = Integer.parseInt(request.getParameter("role"));

            Role role = roleService.getById(roleId);

            User user = new User(Integer.parseInt(id), firstName, lastName, email, password, role);
            userService.update(user);


            session.setAttribute("success", "User updated successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect("admin");
    }

    // Delete a user
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String id = request.getParameter("id");
            userService.delete(Integer.parseInt(id));

            session.setAttribute("success", "User deleted successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }
        response.sendRedirect("admin");
    }

    @Override
    public void destroy() {
        userService = null;
        roleService = null;
    }
}