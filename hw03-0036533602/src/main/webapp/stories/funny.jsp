<%@ page import="java.util.Random" %>
<%@ page import="java.awt.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%!
  public String getColor() {
    /*final String[] colors = new String[]{"#c2571a", "#0d3d56", "#107896", "#c02f1d", "#93a661", "#000000","B3C99C",
            "77037B"};

    Random random = new Random();
    int index = random.nextInt(0, colors.length - 1);

    return colors[index];*/

    Random random = new Random();

    int r = random.nextInt(0,255);
    int g = random.nextInt(0,255);
    int b = random.nextInt(0,255);

    StringBuilder color = new StringBuilder();

    color.append("#").append(Integer.toHexString(r)).append(Integer.toHexString(g)).append(Integer.toHexString(b));
    return color.toString();
  }
%>
<html>
  <head>
	<title>Funny stories</title>
    <style>
      p{
        color: <%= getColor()%>;
      }
    </style>
  </head>
  <body>
    <p>A customer walked into my clothing shop and asked to see the pants that were advertised in the paper that day. “We don’t have an ad in the paper today,” I told her. She insisted I was wrong, so I got a copy of the paper, and we went through it, eventually landing on an ad for pants from another local store. Exasperated, the customer glared at me and said, “In my newspaper, the ad was for this store!” —Edward Oppenheimer</p>
    <p>I loved the dress that I bought at a flea market. It fit perfectly, and the skirt was a swirl of intricate pleats. I wore it confidently to an evening party and glowed when a woman exclaimed, “Oh, how stunning!” Yes, I was grinning from ear to ear, until she added cheerfully, “Hang on to it, honey. Pleats will come back someday.” —Mary Lou Wickham</p>
    <p>In my junior year of high school, this guy asked me on a date. He rented a Redbox movie and made a pizza. We were watching the movie and the oven beeped so the pizza was done. He looked me dead in the eye and said, “This is the worst part.” I then watched this boy open the oven and pull the pizza out with his bare hands, rack and all, screaming at the top of his lungs. We never had a second date.</p>
    <p>I failed the first quarter of a class in middle school, so I made a fake report card. I did this every quarter that year. I forgot that they mail home the end-of-year cards, and my mom got it before I could intercept with my fake. She was PISSED—at the school for their error. The teacher also retired that year and had already thrown out his records, so they had to take my mother’s “proof” (the fake ones I made throughout the year) and “correct” the “mistake.” I’ve never told her the truth.</p>
  </body>
</html>
