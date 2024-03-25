package org.example.servlet;

import org.example.dao.RoleDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.model.User;
import org.example.service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AuthenticationServlet", value = "/login")
public class AuthenticationServlet extends HttpServlet {
    private UserService userService;

    private RoleService roleService;

    @Override
    public void init() {
        roleService = new RoleService(new RoleDAOImpl());
        userService = new UserService(new UserDAOImpl(), roleService);
    }


    // Login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user;
        try{
            user = userService.login(email,password);
        }catch (Exception e){
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        request.getSession().setAttribute("user", user);

        switch (user.getRole().getId()) {
            case 1:
                response.sendRedirect("./admin");
                return;
            case 2:
                response.sendRedirect("./instructor");
                return;
            case 3:
                response.sendRedirect("./student");
                return;
        }
    }

    // login view
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") != null) {
            switch (((User) request.getSession().getAttribute("user")).getRole().getId()) {
                case 1:
                    response.sendRedirect("./admin");
                    return;
                case 2:
                    response.sendRedirect("./instructor");
                    return;
                case 3:
                    response.sendRedirect("./student");
                    return;
            }
        }

        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect("/");
            return;
        }

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

}