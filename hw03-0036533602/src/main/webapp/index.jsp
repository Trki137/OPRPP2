<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Title</title>
        <style>
            a{
                text-decoration: none;
                cursor: pointer;
                color: black;
            }
            a:link{
                text-decoration: none;
            }
            a:hover{
                text-decoration: none;
            }
            a:active{
                text-decoration: none;
            }

            .problem-container{
                display: flex;
                flex-direction: column;
                row-gap: 10px;
            }
            .problem{
                margin-top: 20px;
            }
            .input-container{
                margin-top: 5px;
                display: flex;
                flex-direction: column;
                gap: 5px;
            }
        </style>
    </head>
    <body style="background-color: ${pickedBgColor}">
        <div class="problem-container">
            <div class="problem problem-1">
                <a href="<c:url value="colors.jsp"/> ">Background color chooser</a>
            </div>
            <div class="problem problem-2">
                <a href="<c:url value="/trigonometric?a=0&b=90"/>">Link with params a = 0deg and b = 90deg</a>
                <form action="<c:url value="/trigonometric" /> " method="GET">
                    <div class="input-container">
                        <label>
                            Početni kut:
                            <input type="number" name="a" min="0" max="360" step="1" value="0">
                        </label>
                        <label>
                            Završni kut:
                            <input type="number" name="b" min="0" max="360" step="1" value="360">
                        </label>
                        <div>
                            <input type="submit" value="Tabeliraj">
                            <input type="reset" value="Reset">
                        </div>
                    </div>
                </form>
            </div>

            <div class="problem problem-3">
                <a href="<c:url value="/stories/funny.jsp"/>">Funny stories</a>
            </div>

            <div class="problem problem-4">
                <a href="<c:url value="/report.jsp"/>">OS report</a>
            </div>

            <div class="problem problem-5">
                <a href="<c:url value="/powers?a=1&b=100&n=3"/>">Generate excel</a>
            </div>

            <div class="problem problem-6">
                <a href="<c:url value="/appinfo.jsp"/>">Server runtime</a>
            </div>

            <div class="problem problem-6">
                <a href="<c:url value="/glasanje"/>">Glasaj za omiljeni bend</a>
            </div>
        </div>
    </body>
</html>
