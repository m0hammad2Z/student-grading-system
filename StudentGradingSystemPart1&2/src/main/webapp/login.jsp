<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>

    <style>
        .error {
            color: red;
        }
    </style>
</head>
<body>
    <% if(request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>

    <h1>Login</h1>

    <form action="<c:url value='/login' />" method="post">
        <input type="email" name="email" placeholder="Email" required="required" />
        <input type="password" name="password" placeholder="Password" required="required" />
        <button type="submit">Login</button>
    </form>
</body>
</html>
