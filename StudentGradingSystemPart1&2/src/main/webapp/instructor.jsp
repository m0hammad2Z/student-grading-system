<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Instructor Page</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 15px;
            text-align: left;
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

    <h2>Personal Information</h2>
    <p>Name: ${instructor.firstName} ${instructor.lastName}</p
    <p>Email: ${instructor.email}</p>
    <p>Id: ${instructor.id}</p>

    <!-----------------Courses---------------- -->
    <h1>Courses</h1>
    <table>
        <tr>
            <th>Course ID</th>
            <th>Course Name</th>
            <th>Action</th>
        </tr>
        <c:forEach var="course" items="${courses}">
            <tr>
                <td>${course.id}</td>
                <td>${course.name}</td>
                <th>
                    <button onclick="createAssessment(${course.id})">Create Assessment</button>
                </th>
            </tr>
        </c:forEach>
    </table>

    <!-----------------Assessments------------------>

    <h1>Assessments</h1>
    <table>
        <tr>
            <th>Assessment ID</th>
            <th>Course name</th>
            <th>Assessment Name</th>
            <th>Assessment Type</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="assessment" items="${assessments}">
            <tr>
                <td>${assessment.id}</td>
                <td>${assessment.course.name}</td>
                <td>${assessment.name}</td>
                <td>${assessment.type}</td>
                <th>
                    <button onclick="updateAssessment(${assessment.id}, '${assessment.name}', '${assessment.type}', ${assessment.course.id})">Update Assessment</button>
                    <form action="assessments" method="post">
                        <input type="hidden" name="_method" value="DELETE">
                        <input type="hidden" name="assessmentId" value="${assessment.id}">
                        <input type="submit" value="Delete Assessment">
                    </form>
                    <a href="grades?assessmentId=${assessment.id}&courseId=${assessment.course.id}">Grade Assessment</a>
                </th>
            </tr>
        </c:forEach>
    </table>

    <h2>Create Assessment</h2>
    <form action="assessments" method="post">
        <label for="courseId">Course ID:</label>
        <input type="text" id="courseId" name="courseId">
        <br>
        <label for="assessmentName">Assessment Name:</label>
        <input type="text" id="assessmentName" name="assessmentName">
        <br>
        <label for="assessmentType">Assessment Type:</label>
        <c:forEach var="type" items="${assessmentTypes}">
            <input type="radio" id="${type}" name="assessmentType" value="${type}">
            <label for="${type}">${type}</label>
        </c:forEach>
        <br>
        <input type="submit" value="Create Assessment">
    </form>

     <h2>Update Assessment</h2>
    <form action="assessments" method="post">
        <label for="assessmentId">Assessment ID:</label>
        <input type="text" id="UAssessmentId" name="assessmentId">
        <br>
        <label for="courseId">Course ID:</label>
        <input type="text" id="UCourseId" name="courseId">
        <br>
        <label for="assessmentName">Assessment Name:</label>
        <input type="text" id="UAssessmentName" name="assessmentName">
        <br>
        <label for="assessmentType">Assessment Type:</label>
        <c:forEach var="type" items="${assessmentTypes}">
            <input type="radio" id="U${type}" name="assessmentType" value="${type}">
            <label for="${type}">${type}</label>
        </c:forEach>
        <br>
        <input type="submit" value="Update Assessment">
    </form>

    <!------------------Add/Remove From Course------------------->
    <h2>Add/Remove From Course</h2>
    <form action="enroll" method="post">
        <label for="ECourseId">Course ID:</label>
        <SELECT id="ECourseId" name="courseId">
            <c:forEach var="course" items="${courses}">
                <option value="${course.id}">${course.name}</option>
            </c:forEach>
        </SELECT>
        <br>
        <label for="EStudentIds">Student ID:</label>
        <SELECT id="EStudentIds" name="studentIds[]" multiple>
        <SELECT>
        <input type="hidden" id="EInstructorId" name="instructorId" value="${instructor.id}">
        <br>
        <input type="submit" value="Add/Remove From Course">
    </form>

</body>
    <script>
        function createAssessment(courseId) {
            document.getElementById("courseId").value = courseId;
        }

        function updateAssessment(assessmentId, assessmentName, assessmentType, courseId) {
            document.getElementById("UAssessmentId").value = assessmentId;
            document.getElementById("UAssessmentName").value = assessmentName;
            document.getElementById("U" + assessmentType).checked = true;
            document.getElementById("UCourseId").value = courseId;
        }

        const courseId = document.getElementById("ECourseId");
        const studentIds = document.getElementById("EStudentIds");
        courseId.addEventListener("change", function() {
            fetch("./enroll?id=" + courseId.value)
                .then(response => response.json())
                .then(data => {
                   const allStudents = data.allStudents;
                   const courseStudents = data.courseStudents;
                     studentIds.innerHTML = "";
                        allStudents.forEach(student => {
                            const option = document.createElement("option");
                            option.value = student.id;
                            option.text = student.name;
                            if (courseStudents.some(courseStudent => courseStudent.id === student.id)) {
                                option.selected = true;
                            }
                            studentIds.appendChild(option);
                        });
                });
        });
    </script>
</html>