<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/header.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/index.css">
</head>
<body>
<header class="my-header">
    <c:if test="${sessionScope['current.user.id'] != null}">
        <div class="nav-container">
            <a class="name" href="<c:url value="/servleti/main"/> ">${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']}</a>
            <a class="name" href="<c:url value="/servleti/author/${sessionScope['current.user.nick']}/new"/> " >Create new entry</a>
            <a class="name" href="<c:url value="/servleti/author/${sessionScope['current.user.nick']}"/> ">My blogs</a>
        </div>
        <a class="signout" href="<c:url value='/servleti/signout'/>">Sign out</a>
    </c:if>
    <c:if test="${sessionScope['current.user.id'] == null}">
        <div class="nav-container">
            <a class="name" href="<c:url value="/servleti/main"/> ">Home</a>
        </div>
    </c:if>
</header>
</body>
</html>
