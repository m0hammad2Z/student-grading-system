<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <head>
        <meta charset="UTF-8">
        <title>Student Page</title>
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

            th {
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

    <h1>Student Page</h1>

    <h2>Student Information</h2>
    <p>ID: ${user.id}</p>
    <p>Name: ${user.firstName} ${user.lastName}</p>
    <p>Email: ${user.email}</p>


    <h2>Courses</h2>
    <!-- Display a list of courses the student is enrolled in -->
    <table>
        <!-- Table headers -->
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Average Grade</th>

        </tr>
        <!-- Table data -->
        <!-- Loop through the courses and display each one in a table row -->
        <c:forEach var="entry" items="${courseAverage}">
            <tr>
                <td>${entry.key.id}</td>
                <td>${entry.key.name}</td>
                <td>${entry.value}</td>
            </tr>
        </c:forEach>
    </table>

    <h2>Grades</h2>
    <!-- Display a list of grades -->
    <table>
        <!-- Table headers -->
        <tr>
            <th>Course ID</th>
            <th>Course Name</th>
            <th>Assessment ID</th>
            <th>Assessment Name</th>
            <th>Grade</th>
        </tr>
        <!-- Table data -->
        <!-- Loop through the grades and display each one in a table row -->
        <c:forEach var="grade" items="${grades}">
            <tr>
                <td>${grade.assessment.course.id}</td>
                <td>${grade.assessment.course.name}</td>
                <td>${grade.assessment.id}</td>
                <td>${grade.assessment.name}</td>
                <td>${grade.grade}</td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>