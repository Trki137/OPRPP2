<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
	<title>Error</title>
  </head>
  <body>
  <jsp:include page="header.jsp">
    <jsp:param name="userId" value="${sessionScope['current.user.id']}" />
  </jsp:include>
  <main>
    <h1>${error}</h1>
  </main>
  </body>
</html>
