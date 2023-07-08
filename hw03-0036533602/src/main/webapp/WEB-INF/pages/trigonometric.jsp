<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Trigonometric function table</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/table.css"/>">
    </head>
    <body>
        <table>
            <thead>
                <tr>
                    <th>degree</th>
                    <th>sin</th>
                    <th>cos</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="number" begin="${from}" end="${to}" step="1">
                <tr>
                    <td>${number}</td>
                    <td>${Math.sin(number.doubleValue())}</td>
                    <td>${Math.cos(number.doubleValue())}</td>
                </tr>
            </c:forEach>
            </tbody>


        </table>
    </body>
</html>
