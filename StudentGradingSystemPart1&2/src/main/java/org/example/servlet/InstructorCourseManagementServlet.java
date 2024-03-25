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
import java.util.List;

@WebServlet(name = "InstructorCourseManagementServlet", value = "/instructorCourse")
public class InstructorCourseManagementServlet extends HttpServlet {
    CourseService courseService;
    UserService userService;

    public void init(ServletConfig config) throws ServletException {
        courseService = new CourseService(new CourseDAOImpl());
        userService = new UserService(new UserDAOImpl(), new RoleService(new RoleDAOImpl()));
    }


    // Return a json that contains all the instructors that teach the course
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int courseId = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0;

        List<User> courseInstructors = userService.getAllInstructors(courseId);
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (User instructor : courseInstructors) {
            JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            jsonObject.add("id", instructor.getId());
            jsonObject.add("name", instructor.getFirstName() + " " + instructor.getLastName());
            jsonArray.add(jsonObject);
        }

        List<User> allInstructors = userService.getAllUsersByRole(2);
        JsonArrayBuilder allInstructorsArray = Json.createArrayBuilder();
        for (User instructor : allInstructors) {
            JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            jsonObject.add("id", instructor.getId());
            jsonObject.add("name", instructor.getFirstName() + " " + instructor.getLastName());
            allInstructorsArray.add(jsonObject);
        }

        JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        jsonObject.add("courseInstructors", jsonArray);
        jsonObject.add("allInstructors", allInstructorsArray);

        response.setContentType("application/json");
        response.getWriter().write(jsonObject.build().toString());
    }

    // Modify the instructorCourses (Assign, Unassign)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int courseId = request.getParameter("courseId") != null ? Integer.parseInt(request.getParameter("courseId")) : 0;
            String[] instructorIds = request.getParameterValues("instructorIds[]");
            List<Integer> instructorIdsList = new ArrayList<>();
            if (instructorIds == null) {
                throw new IllegalArgumentException("Instructor is required");
            }

            for (String instructorId : instructorIds) {
                instructorIdsList.add(Integer.parseInt(instructorId));
            }

            List<User> courseInstructors = userService.getAllInstructors(courseId);
            List<Integer> courseInstructorsIds = new ArrayList<>();
            for (User instructor : courseInstructors) {
                courseInstructorsIds.add(instructor.getId());
            }
            courseService.removeInstructor(courseInstructorsIds, courseId);
            courseService.assignInstructor(instructorIdsList, courseId);

            session.setAttribute("success", "Instructors assigned successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }
        response.sendRedirect("admin");
    }
}