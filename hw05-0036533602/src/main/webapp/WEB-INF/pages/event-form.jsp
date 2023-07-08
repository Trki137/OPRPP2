<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Event form</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/form.css">
    </head>
    <body>
    <jsp:include page="header.jsp">
        <jsp:param name="userId" value="${sessionScope['current.user.id']}" />
    </jsp:include>
        <div class="container-wrapper">
            <div class="container">
                <form class="login-form" action="<c:url value="/servleti/author/"/>" method="post">
                    <c:if test="${blog.id != null}">
                        <input type="hidden" name="id" value="${blog.id}">
                    </c:if>
                    <div class="input-container">
                        <input type="text" placeholder="Title" name="title" value="${blog.title}"/>
                        <c:if test="${title != null}">
                            <p class="input-error">${title}</p>
                        </c:if>
                    </div>
                    <div class="input-container">
                        <textarea placeholder="Text..." name="text" cols="31" rows="8">${blog.text}</textarea>
                        <c:if test="${text != null}">
                            <p class="input-error">${text}</p>
                        </c:if>
                    </div>
                    <input type="submit" value="Post" class="btn" />
                </form>
            </div>
        </div>
    </body>
</html>
