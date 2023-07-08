<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Glasanje</title>
</head>
<body>
<ol>
    <c:forEach items="${polls}" var="poll" >
        <li>
            <a href="<c:url value="/servleti/glasanje?pollID=${poll.id}"/>">
                    ${poll.title}
            </a>
        </li>
    </c:forEach>
</ol>
</body>
</html>
