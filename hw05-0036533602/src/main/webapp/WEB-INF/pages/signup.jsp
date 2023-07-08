<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Sign up</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/form.css">
</head>
<body>
<jsp:include page="header.jsp">
  <jsp:param name="userId" value="${sessionScope['current.user.id']}" />
</jsp:include>
<main>
  <div class="container-wrapper">
    <div class="container">
      <form class="login-form" action="<c:url value="/servleti/register"/>" method="post">
        <div class="input-container">
          <input type="text" placeholder="First name" name="firstname" value="${user.firstName}"/>
          <c:if test="${firstname != null}">
            <p class="input-error">${firstname}</p>
          </c:if>
        </div>
        <div class="input-container">
          <input type="text" placeholder="Last name" name="lastname" value="${user.lastName}"/>
          <c:if test="${lastname != null}">
            <p class="input-error">${lastname}</p>
          </c:if>
        </div>
        <div class="input-container">
          <input type="text" placeholder="Email" name="email" value="${user.email}"/>
          <c:if test="${email != null}">
            <p class="input-error">${email}</p>
          </c:if>
        </div>
        <div class="input-container">
          <input type="text" placeholder="Nickname" name="nick" value="${user.nick}"/>
          <c:if test="${nick != null}">
            <p class="input-error">${nick}</p>
          </c:if>
        </div>
        <div class="input-container">
          <input type="password" placeholder="Password" name="password" value="${user.password}">
          <c:if test="${password != null}">
            <p class="input-error">${password}</p>
          </c:if>
        </div>
        <div>
          <input type="submit" value="Sign up" class="btn" />
        </div>
      </form>
      <a class="sign-up-link" href="<c:url value="/servleti/main" />">Already have an account? Sign in here</a>
    </div>
  </div>
</main>
</body>
</html>
