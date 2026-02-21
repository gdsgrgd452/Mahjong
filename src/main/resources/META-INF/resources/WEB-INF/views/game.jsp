<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Game</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/tilesDisplay.css" type="text/css"> <!-- Linking the css in -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/tableDisplay.css" type="text/css">
</head>
<body>
    <div class="game-container">
        <%-- NEW: Center Stage for the Just Discarded Tile --%>
        <c:if test="${justDiscardedTile != null}">
            <div class="center-stage">
                <div class="tile tile-large">
                    <span class="suit-label">${justDiscardedTile.suit}</span>
                    <c:if test="${justDiscardedTile.number != null && justDiscardedTile.number != 0}">
                        <span class="number-label">${justDiscardedTile.number}</span>
                    </c:if>
                </div>
            </div>
        </c:if>
        <div class="mahjong-board">
            <div class="live-section">
                <c:forEach items="${liveTiles}" var="tile">
                    <div class="tile">
                        <span class="suit-label">${tile.suit}</span>
                        <c:if test="${tile.number != null && tile.number != 0}">
                            <span class="number-label">${tile.number}</span>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
            <div class="discarded-section">
                <c:forEach items="${discardedTiles}" var="tile">
                    <div class="tile ${tile.justDiscarded == true ? 'tile-just-discarded' : 'tile'}">
                        <span class="suit-label">${tile.suit}</span>
                        <c:if test="${tile.number != null && tile.number != 0}">
                            <span class="number-label">${tile.number}</span>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="hands-section">
            <c:forEach items="${players}" var="player">
                <c:set var="actionToTake" value="${player.actionToTake == true ? 'player-has-action' : ''}" />
                <c:set var="playerTurn" value="${player.playerId == currentPlayerId ? 'player-hand-turn' : ''}" />
                <div class="player-hand ${playerTurn} ${actionToTake}">
                    <p class="playerId">${player.playerId}</p>
                        <c:forEach items="${player.currentHand}" var="tile">
                            <c:if test="${tile.placed == false}">
                                <%-- Determine the base status class --%>
                                <c:set var="inPungClass" value="${tile.pung != null ? 'tile-in-pung' : 'tile-active'}" />
                                <%-- Appends tile-in-chow if it is in a chow --%>
                                <c:set var="inChowClass" value="${tile.chow != null ? 'tile-in-chow' : ''}" />
                                <%-- Appends 'tile-just-picked' if it was just picked --%>
                                <c:set var="pickedClass" value="${tile.justPickedUp ? ' tile-just-picked' : ''}" />
                                <button type="button"
                                        class="tile ${inPungClass} ${inChowClass} ${pickedClass}"
                                        data-tile-id="${tile.tileId}"
                                        onclick="discardTile(this)">
                                    <span class="suit-label">${tile.suit}</span>
                                    <c:if test="${tile.number != null && tile.number != 0}">
                                        <span class="number-label">${tile.number}</span>
                                    </c:if>
                                </button>
                            </c:if>
                            <c:if test="${tile.placed == true}">
                                <div class="tile tile-placed">
                                    <span class="suit-label">${tile.suit}</span>
                                    <c:if test="${tile.number != null && tile.number != 0}">
                                        <span class="number-label">${tile.number}</span>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:forEach>
                    <p>${fn:length(player.currentHand)}</p>
                    <div class="action-buttons">
                        <c:set var="pActive" value="${player.actionToTake == 'P' ? 'btn-active' : ''}" />
                        <button class="action-btn ${pActive}" onclick="sendPlayerAction(${player.playerId}, 'pung')">Pung</button>
                        <c:set var="cActive" value="${player.actionToTake == 'C' ? 'btn-active' : ''}" />
                        <button class="action-btn ${cActive}" onclick="sendPlayerAction(${player.playerId}, 'chow')">Chow</button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <script>
        // Updated discard function to hit the specific endpoint
        function discardTile(buttonElement) {
            const tileId = buttonElement.getAttribute('data-tile-id');
            // Using the specific /game/discard endpoint
            sendAction('/game/discard', { tileId: tileId });
        }

        // New generic function for Pungs and Chows
        function sendPlayerAction(playerId, actionType) {

            // Determine URL based on action type
            const url = actionType === 'pung' ? '/game/pung' : '/game/chow';

            // Build params (Chow might need extra logic to select tiles first!)
            const params = { playerId: playerId };

            sendAction(url, params);
        }

        function sendAction(url, data) {
            const formData = new URLSearchParams();
            for (const key in data) {
                formData.append(key, data[key]);
            }

            // Using pageContext path ensures the URL is correct relative to your app
            fetch('${pageContext.request.contextPath}' + url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        location.reload(); // Reloads page to show new game state
                    } else {
                        response.text().then(text => alert("Action failed: " + text));
                    }
                })
                .catch(err => console.error('Fetch error:', err));
        }
    </script>
</body>
</html>