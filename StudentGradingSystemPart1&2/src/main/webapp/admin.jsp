<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Page</title>

  <style>
          body {
              font-family: Arial, sans-serif;
              background-color: #f4f4f4;
              margin: 0;
              padding: 0;
          }
          .container {
              display: flex;
              justify-content: space-between;
              gap: 20px;
              margin: 20px;
              padding: 20px;
              background-color: white;
              border-radius: 5px;
              box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
          }
          .container2 {
              display: flex;
              flex-direction: column;
              gap: 20px;
          }
          table {
              border-collapse: collapse;
              width: 100%;
          }
          th, td {
              border: 1px solid #ddd;
              padding: 8px;
              text-align: left;
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
<body>
    <h1>Admin Page</h1>

<% if(session.getAttribute("success") != null) { %>
    <p class="success"><%= session.getAttribute("success") %></p>
    <% session.removeAttribute("success"); %>
<% } %>

<% if(session.getAttribute("error") != null) { %>
    <p class="error"><%= session.getAttribute("error") %></p>
    <% session.removeAttribute("error"); %>
<% } %>

    <div class="container">
        <div>
        <table>
                 <h2>Users</h2>
                     <tr>
                         <th>ID</th>
                         <th>First Name</th>
                         <th>Last Name</th>
                         <th>Email</th>
                         <th>Role</th>
                         <th>Actions</th>
                     </tr>

                     <c:forEach var="user" items="${users}">
                         <tr>
                             <td>${user.id}</td>
                             <td>${user.firstName}</td>
                             <td>${user.lastName}</td>
                             <td>${user.email}</td>
                             <td>${user.role.name}</td>
                             <td>
                                 <button onclick="updateUser(${user.id}, '${user.firstName}', '${user.lastName}', '${user.email}', '${user.role.id}')">Update</button>
                                 <form action="users" method="post">
                                     <input type="hidden" name="_method" value="DELETE">
                                     <input type="hidden" name="id" value="${user.id}">
                                     <input type="submit" value="Delete">
                                 </form>
                             </td>
                         </tr>
                     </c:forEach>
                 </table>
        </div>

             <div class="container2">

              <!-- Forms for creating new users -->
                 <h2>Create User</h2>
                 <form action="users" method="post">
                     <label for="CFirstName">First Name:</label><br>
                     <input type="text" id="CFirstName" name="firstName"><br>
                     <label for="CLastName">Last Name:</label><br>
                     <input type="text" id="CLastName" name="lastName"><br>
                     <label for="CEmail">Email:</label><br>
                     <input type="text" id="CEmail" name="email"><br>
                     <label for="CPass">Password:</label><br>
                     <input type="password" id="CPass" name="password"><br>
                     <label for="CRole">Role:</label><br>
                     <SELECT id="CRole" name="role">
                         <c:forEach var="role" items="${roles}">
                             <option value="${role.id}">${role.name}</option>
                         </c:forEach>
                     </SELECT><br>
                     <input type="submit" value="Create User">
                 </form>
                <hr>
             <!-- Update form for users -->
             <h2>Update User</h2>
             <form action="users" method="post" id="updateUserForm">
                 <input type="hidden" id="_method" name="_method" value="PUT">
                 <input type="hidden" id="UUserId" name="id" value="${user.id}">
                 <label for="UFirstName">First Name:</label><br>
                 <input type="text" id="UFirstName" name="firstName" value="${user.firstName}"><br>
                 <label for="ULastName">Last Name:</label><br>
                 <input type="text" id="ULastName" name="lastName" value="${user.lastName}"><br>
                 <label for="UEmail">Email:</label><br>
                 <input type="text" id="UEmail" name="email" value="${user.email}"><br>
                 <label for="UPass">Password:</label><br>
                 <input type="password" id="UPass" name="password"><br>
                 <label for="URole">Role:</label><br>
                 <SELECT id="URole" name="role">
                     <c:forEach var="role" items="${roles}">
                         <option value="${role.id}">${role.name}</option>
                     </c:forEach>
                 </SELECT><br>
                 <input type="submit" value="Update User">
             </form>
        </div>

            <!-- Courses Table -->
            <div class="container2">
                <table>
                    <h2>Courses</h2>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                    <c:forEach var="course" items="${courses}">
                        <tr>
                            <td>${course.id}</td>
                            <td>${course.name}</td>
                            <td>
                                <button onclick="updateCourse(${course.id}, '${course.name}')">Update</button>
                                <form action="courses" method="post">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <input type="hidden" name="id" value="${course.id}">
                                    <input type="submit" value="Delete">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>


        <div class="container2">
            <!-- Update form for courses -->
            <h2>Update Course</h2>
            <form action="courses" method="post" id="updateCourseForm">
                <input type="hidden" id="_method" name="_method" value="PUT">
                <input type="hidden" id="UCourseId" name="id">
                <label for="UCourseName">Name:</label><br>
                <input type="text" id="UCourseName" name="name"><br>
                <input type="submit" value="Update Course">
            </form>

            <hr>

            <!-- Form for creating new courses -->
            <h2>Create Course</h2>
            <form action="courses" method="post">
                <label for="CCourseName">Name:</label><br>
                <input type="text" id="CCourseName" name="name"><br>
                <input type="submit" value="Create Course">
            </form>

            <hr>

            <!-- Form to update the Instructors that teach a course -->
            <h2>Assign Instructor to Course</h2>
            <form action="instructorCourse" method="post">
                <label for="ACourseIds">Courses:</label><br>
                <select id="ACourseId" name="courseId">
                    <c:forEach var="course" items="${courses}">
                        <option value="${course.id}">${course.name}</option>
                    </c:forEach>
                </select><br>
                <label for="AInstructorIds">Instructor:</label><br>
                <select id="AInstructorIds" name="instructorIds[]" multiple>
                </select><br>
                <input type="submit" value="Modify Instructor Courses">
            </form>
        </div>
    </div>
</body>

<script>
    //Load the user data into the update form when the user clicks the update button
    function updateUser(id, firstName, lastName, email, role) {
        document.getElementById('UUserId').value = id;
        document.getElementById('UFirstName').value = firstName;
        document.getElementById('ULastName').value = lastName;
        document.getElementById('UEmail').value = email;
        document.getElementById('URole').value = role;
    }

    //Load the course data into the update form when the user clicks the update button
    function updateCourse(id, name) {
        document.getElementById('UCourseId').value = id;
        document.getElementById('UCourseName').value = name;
    }


   const assignCoursesSelect = document.getElementById('ACourseId');
   assignCoursesSelect.addEventListener('change', function() {
       var courseId = this.value;
         fetch('./instructorCourse?id=' + courseId)
              .then(response => response.json())
              .then(data => {
                const allInstructors = data.allInstructors;
                const courseInstructors = data.courseInstructors;

                const select = document.getElementById('AInstructorIds');
                select.innerHTML = '';

                allInstructors.forEach(instructor => {
                    const option = document.createElement('option');
                    option.value = instructor.id;
                    option.text = instructor.name;

                    if (courseInstructors.some(courseInstructor => courseInstructor.id === instructor.id)) {
                        option.selected = true;
                    }

                    select.appendChild(option);
                });
            });
    });

</script>


</html>