<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
      <title>Entry</title>
      <style>

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
      <div class="list-container">
          <c:if test="${blogs.size() == 0}">
              <h1 >You don't have any blog created</h1>
          </c:if>

        <c:forEach items="${blogs}" var="blog" >
          <li>
              <a href="<c:url value="/servleti/author/${blog.user.nick}/${blog.id}"/> ">
                      ${blog.title} <c:if test="${blog.user.id == sessionScope['current.user.id']}">
                          <a href="<c:url value="/servleti/author/${blog.user.nick}/${blog.id}/edit"/>">Update</a>
                      </c:if>
              </a>
          </li>
        </c:forEach>
      </div>
    </main>
    </body>
</html>
