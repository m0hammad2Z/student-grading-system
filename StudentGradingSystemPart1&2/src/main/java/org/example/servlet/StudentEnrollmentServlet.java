package org.example.servlet;
import org.example.dao.CourseDAOImpl;
import org.example.dao.RoleDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.service.CourseService;
import org.example.service.RoleService;
import org.example.service.UserService;

import javax.json.Json;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import org.example.model.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "StudentEnrollmentServlet", value = "/enroll")
public class StudentEnrollmentServlet extends HttpServlet {
    CourseService courseService;
    UserService userService;

    public void init(ServletConfig config) throws ServletException {
        courseService = new CourseService(new CourseDAOImpl());
        userService = new UserService(new UserDAOImpl(),  new RoleService(new RoleDAOImpl()));
    }

    // Get list of all students and list of enrolled students for a course as json
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int courseId = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0;

        List<User> courseStudents = userService.getAllStudents(courseId);
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (User student : courseStudents) {
            JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            jsonObject.add("id", student.getId());
            jsonObject.add("name", student.getFirstName() + " " + student.getLastName());
            jsonArray.add(jsonObject);
        }

        List<User> allStudents = userService.getAllUsersByRole(3);
        JsonArrayBuilder allStudentsArray = Json.createArrayBuilder();
        for (User student : allStudents) {
            JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            jsonObject.add("id", student.getId());
            jsonObject.add("name", student.getFirstName() + " " + student.getLastName());
            allStudentsArray.add(jsonObject);
        }

        JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        jsonObject.add("courseStudents", jsonArray);
        jsonObject.add("allStudents", allStudentsArray);

        response.setContentType("application/json");
        response.getWriter().print(jsonObject.build());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String[] studentIds = request.getParameterValues("studentIds[]");

            if(studentIds == null) throw new IllegalArgumentException("No students selected");

            int courseId = request.getParameter("courseId") != null ? Integer.parseInt(request.getParameter("courseId")) : 0;
            User instructor = (User) request.getSession().getAttribute("user");

            List<User> courseStudents = userService.getAllStudents(courseId);
            Set<Integer> courseStudentsIds = new HashSet<>();
            for (User student : courseStudents) {
                courseStudentsIds.add(student.getId());
            }

            Set<Integer> studentIdsToEnroll = new HashSet<>();
            for (String studentId : studentIds) {
                int id = Integer.parseInt(studentId);
                if (!courseStudentsIds.contains(id)) {
                    studentIdsToEnroll.add(id);
                } else {
                    courseStudentsIds.remove(id);
                }
            }

            courseService.unrollStudent(new ArrayList<>(courseStudentsIds), courseId, instructor.getId());
            courseService.enrollStudent(new ArrayList<>(studentIdsToEnroll), courseId, instructor.getId());

            session.setAttribute("success", "Students enrolled successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }
        response.sendRedirect("instructor");
    }
}
