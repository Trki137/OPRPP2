<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Glasanje</title>
</head>
<body>
<p>${message}</p>
<ol>
    <c:forEach items="${pollOptions}" var="option" >
        <li>
            <a href="<c:url value="/servleti/glasanje-glasaj?optionID=${option.id}"/>">
                    ${option.optionTitle}
            </a>
        </li>
    </c:forEach>
</ol>
</body>
</html>
