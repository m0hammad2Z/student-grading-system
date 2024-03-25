package org.example.servlet;

import org.example.dao.RoleDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.model.User;
import org.example.service.RoleService;
import org.example.service.UserService;

import javax.servlet.*;
import java.io.IOException;
import javax.servlet.http.*;

public class AuthorizationFilter implements Filter {
    private UserService userService;
    private RoleService roleService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        roleService = new RoleService(new RoleDAOImpl());
        userService = new UserService(new UserDAOImpl(), roleService);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();
        if (path.equals("/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        roleService = null;
        userService = null;
    }
}
