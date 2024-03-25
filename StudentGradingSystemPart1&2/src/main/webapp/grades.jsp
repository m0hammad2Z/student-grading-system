<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Grade Assessments</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .success {
            color: green;
        }

        .error {
            color: red;
        }
    </style>

</head>
<body>
 <% if(session.getAttribute("success") != null) { %>
     <p class="success"><%= session.getAttribute("success") %></p>
     <% session.removeAttribute("success"); %>
 <% } %>

 <% if(session.getAttribute("error") != null) { %>
     <p class="error"><%= session.getAttribute("error") %></p>
     <% session.removeAttribute("error"); %>
 <% } %>

    <h1>Grade Assessments</h1>
    <h2> Course: ${course.name}</h2>
    <h2>Assessment: ${assessment.name}</h2>

    <table>
        <tr>
            <th>Student ID</th>
            <th>Student First Name</th>
            <th>Student Last Name</th>
            <th>Student Email</th>
            <th>Student Grade</th>
            <th>Grade</th>
        </tr>

        <c:forEach items="${grades}" var="grade">
            <tr>
                <td>${grade.student.id}</td>
                <td>${grade.student.firstName}</td>
                <td>${grade.student.lastName}</td>
                <td>${grade.student.email}</td>
                <td>${grade.grade}</td>
                <td>
                    <form action="grades" method="post">
                        <input type="hidden" name="studentId" value="${grade.student.id}" />
                        <input type="hidden" name="assessmentId" value="${assessment.id}" />
                        <input type="hidden" name="courseId" value="${course.id}" />
                        <input type="hidden" name="gradeId" value="${grade.id}" />
                        <input type="text" name="grade" value="${grade.grade}" />
                        <input type="submit" value="Grade" />
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
