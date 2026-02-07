<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tiles Display</title>
</head>

<body>
<h1>Showing all tiles</h1>
<hr>
<div class="mahjong-board">
    <c:forEach items="${tiles}" var="tile">
        <div class="tile">
            <span class="suit-label">${tile.suit}</span>

                <%-- Only shows number if it exists --%>
            <c:if test="${not empty tile.number}">
                <span class="number-label">${tile.number}</span>
            </c:if>
        </div>
    </c:forEach>
</div>

</body>
</html>