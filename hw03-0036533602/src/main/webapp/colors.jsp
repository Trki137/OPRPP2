<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <a href="<c:url value="/setcolor?bgColor=00ffff" />">CYAN</a>
    <a href="<c:url value="/setcolor?bgColor=ff0000" />">RED</a>
    <a href="<c:url value="/setcolor?bgColor=ffffff" />">WHITE</a>
    <a href="<c:url value="/setcolor?bgColor=00ff00" />">GREEN</a>
</body>
</html>
