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
		<c:forEach items="${results}"  var="votes">
			<tr>
				<td>${votes.bandName}</td>
				<td>${votes.votes}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<h2>Grafički prikaz rezultata</h2>
	<img src="<c:url value="glasanje-grafika"/>" alt="No image to show"/>
	<h2>Rezultati u XLS formatu</h2>
	<p>Rezultati u XLS formatu dostupni su <a href="<c:url value="/glasanje-xls" /> ">ovdje</a></p>

	<h2>Razno</h2>
	<ul>
	<c:forEach items="${winners}" var="winner" >
		<li><a href="${winner.ytLink}">${winner.bandName}</a></li>
	</c:forEach>

	</ul>
</body>
</html>
