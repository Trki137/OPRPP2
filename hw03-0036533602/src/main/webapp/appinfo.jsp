<%@ page import="java.time.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
  public String getTimeFormatted(long startupTime){
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startUp = LocalDateTime.ofInstant(Instant.ofEpochMilli(startupTime), ZoneId.systemDefault());


    Period period = Period.between(startUp.toLocalDate(),now.toLocalDate());

    Duration duration = Duration.between(startUp,now);
    StringBuilder sb = new StringBuilder();

    if(period.getYears() != 0) sb.append(String.format("%d years,",period.getYears()));
    if(period.getMonths() != 0) sb.append(String.format("%d months,",period.getMonths()));
    if(period.getDays() != 0) sb.append(String.format("%d days,",period.getDays()));


    if(duration.toHours() != 0) sb.append(String.format("%d hours,",duration.toHours()));
    if(duration.toMinutes() != 0) sb.append(String.format("%d minutes,",duration.toMinutes() - 60 * duration.toHours()));
    if(duration.toSeconds() != 0) sb.append(String.format("%d seconds",duration.toSeconds() - 60 * duration.toMinutes()));

    return sb.toString();
  }

%>

<html>
  <head>
	<title>Web server working</title>
  </head>
  <body>
    <p>Web server is running for <%=
    getTimeFormatted(Long.parseLong(request.getServletContext().getAttribute("start").toString()))%></p>
  </body>
</html>
