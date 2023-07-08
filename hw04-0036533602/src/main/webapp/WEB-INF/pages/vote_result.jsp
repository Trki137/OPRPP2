<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Rezultati glasanja</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/table.css">
</head>
<body>
<h1>Rezultati glasanja</h1>
<p>Ovo su rezultati glasanja</p>
<table>
    <thead>
    <tr>
        <th>Band</th>
        <th>Votes</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${votingResults}"  var="votes">
        <tr>
            <td>${votes.optionTitle}</td>
            <td>${votes.votesCount}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<h2>Grafički prikaz rezultata</h2>
<img src="<c:url value="/servleti/glasanje-grafika?pollID=${id}"/>" alt="No image to show"/>
<h2>Rezultati u XLS formatu</h2>
<p>Rezultati u XLS formatu dostupni su <a href="<c:url value="/servleti/glasanje-xls?pollID=${id}" /> ">ovdje</a></p>


<h2>Razno</h2>
<ul>
    <c:forEach items="${winners}" var="winner" >
        <li><a href="${winner.optionLink}">${winner.optionTitle}</a></li>
    </c:forEach>
</ul>
</body>
</html>
