<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="stylesheet.css">
    <title>Cosmopolitune</title>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        <%
        String mapJS = (String) request.getAttribute("mapJS");
        out.println(mapJS);
        %>
    </script>
</head>
<body>
<%
String successMessage = (String) request.getAttribute("successMessage");
String playlistEmbed = (String) request.getAttribute("playlistEmbed");
%>
<center>
    <h1>
        <a href="http://lewis-forbes.us-east-2.elasticbeanstalk.com/cosmopolitune" style="color:inherit">Cosmopolitune</a>
    </h1>

    <h3>Here's your map:</h3>
    <div id="regions_div" style="height: 380px; display:inline-block;"></div>
    <div style="display:inline-block;"><% out.println(playlistEmbed); %></div>
    <div style="display:inline-block"><br><br></div>

    <p><% out.println(successMessage); %></p>
    <br><br>
    <div>
    <p>Made by <a href="https://lewisforbes.com/">Lewis Forbes</a></p>
    <p>Code available on <a href="https://github.com/lewisforbes/Cosmopolitune">GitHub</a></p>
    </div>
</center>
</body>
</html>
