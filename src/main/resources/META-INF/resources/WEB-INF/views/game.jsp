<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Game</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/tilesDisplay.css" type="text/css"> <!-- Linking the css in -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/tableDisplay.css" type="text/css">
</head>
<div class="game-container">
    <div class="mahjong-board">
    </div>
    <div class="player-hand">
         <c:forEach items="${playerHand}" var="tile">
             <div class="tile">
                 <span class="suit-label">${tile.suit}</span>
                 <c:if test="${tile.number != null && tile.number != 0}">
                     <span class="number-label">${tile.number}</span>
                 </c:if>
             </div>
         </c:forEach>
    </div>
</div>
<body>

</body>
</html>