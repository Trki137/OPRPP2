<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
	<head>
		<title>Glasanje</title>
	</head>
	<body>
		<h1>Glasanje za omiljeni bend:</h1>
		<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p>
		<ol>
			<c:forEach items="${bands}" var="band" >
				<li>
					<a href="<c:url value="/glasanje-glasaj?id=${band.id}"/>">
						${band.bandName}
					</a>
				</li>
			</c:forEach>
		</ol>
	</body>
</html>
