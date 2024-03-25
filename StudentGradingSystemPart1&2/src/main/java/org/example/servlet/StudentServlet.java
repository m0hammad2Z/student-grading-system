package org.example.servlet;

import org.example.dao.*;
import org.example.model.*;
import org.example.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.*;


@javax.servlet.annotation.WebServlet(name = "StudentServlet", value = "/student")
public class StudentServlet extends HttpServlet {
    private GradeService gradeService;
    private CourseService courseService;


    @Override
    public void init() {
        gradeService = new GradeService(new GradeDAOImpl());
        courseService = new CourseService(new CourseDAOImpl());}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(401);
            return;
        }

        request.setAttribute("user", user);

        List<Grade> grades = gradeService.getAllByStudent(user.getId());
        request.setAttribute("grades", grades);
        List<Course> courses = courseService.getAllStudentCourses(user.getId());
        Map<Course, Double> courseAverage = courseService.getCoursesWithAverage(courses);
        request.setAttribute("courseAverage", courseAverage);

        request.getRequestDispatcher("/student.jsp").forward(request, response);
    }



}