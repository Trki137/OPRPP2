<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Main</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/form.css">
        <style>
            .list-container{
                max-width: 300px;
            }
            a{
                display: inline;
            }
        </style>
    </head>
    <body>
    <jsp:include page="header.jsp">
        <jsp:param name="userId" value="${sessionScope['current.user.id']}" />
    </jsp:include>

        <main>
        <c:if test="${sessionScope['current.user.id'] == null}">
            <div class="container-wrapper">
                <div class="container">
                    <form class="login-form" action="<c:url value="/servleti/signin"/>" method="post">
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
                        <input type="submit" value="Sign in" class="btn" />
                    </form>
                    <a class="sign-up-link" href="<c:url value="/servleti/register" />">Don't have account yet? Sign up here</a>
                </div>
            </div>
        </c:if>

                <div>
                    <c:if test="${authors.size() != 0}">
                        <h1>Other authors</h1>
                    </c:if>
                    <div class="list-container">
                        <c:forEach items="${authors}" var="author" >
                            <li><a href="<c:url value="/servleti/author/${author.nick}"/> ">${author.firstName} ${author.lastName}</a></li>
                        </c:forEach>
                    </div>
                </div>
        </main>
</body>
</html>
