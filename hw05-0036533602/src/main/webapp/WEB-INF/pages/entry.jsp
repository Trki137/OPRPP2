<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>${blog.title}</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/form.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/blog-entry.css">
</head>
<body>
<jsp:include page="header.jsp">
  <jsp:param name="userId" value="${sessionScope['current.user.id']}" />
</jsp:include>
<main>
  <div class="blog-container">
    <div class="blog-data">
      <div class="blog-group">
        <p class="blog-created-by">${blog.user.nick}</p>
        <p class="blog-date"><fmt:formatDate value="${blog.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>

      </div>

      <p class="blog-title">${blog.title}</p>
      <p class="blog-text">${blog.text}</p>
    </div>
    <div class="blog-comments">
      <c:if test="${blog.comments.size() == 0}">
        <p>No comments yet</p>
      </c:if>
      <c:forEach items="${blog.comments}" var="comment">
        <div class="blog-comment">
        <div class="comment-group">
          <p class="comment-created-by">${comment.usersEMail}</p>
          <p class="comment-date"><fmt:formatDate value="${comment.postedOn}" pattern="yyyy-MM-dd HH:mm:ss" /></p>

        </div>
        <p>${comment.message}</p>
        </div>
      </c:forEach>

  <c:if test="${sessionScope['current.user.id'] != null}">
      <div class="blog-add-comment">

        <form method="post" action="<c:url value="/servleti/entry"/>">
            <input type="hidden" name="id" value="${blog.id}">
            <input type="hidden" name="post_nickname" value="${blog.user.nick}">
            <input type="hidden" name="entry_id" value="${blog.id}">
          <div class="input-container">
            <textarea placeholder="Comment..." name="text" rows="4"></textarea>
            <c:if test="${text != null}">
              <p class="input-error">${text}</p>
            </c:if>
            <input type="submit" value="Post comment" class="btn" />
          </div>


        </form>
      </div>
  </c:if>
    </div>
  </div>
</main>
</body>
</html>
