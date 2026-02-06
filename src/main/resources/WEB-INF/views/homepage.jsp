<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Homepage</title>
</head>

<body>
<h1>Welcome</h1>
<hr>
<div class="navBar">
    <div class="courseNav ${activePage == 'coursesDashboard' ? 'active' : ''}">
        <h2>Courses</h2>
    </div>
</div>
</body>
</html>