<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <%
    String mapJS = (String) request.getAttribute("playlistMapJS");
    String redirect = (String) request.getAttribute("redirect");
    String content = "0; url=" + redirect;
    %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="stylesheet.css">
    <title>Cosmopolitune</title>
    <meta http-equiv="Refresh" content="<%=content %>" />

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        <%
        out.println(mapJS);
        %>
    </script>
    <style>
        @media only screen and (min-width: 1001px) {
            #mobile_message {
                display: none;
            }
        }
    </style>
</head>
<body>
<%
String playlistEmbed = (String) request.getAttribute("playlistEmbed");
%>
<center>
    <h1>
        <a href="http://lewis-forbes.us-east-2.elasticbeanstalk.com/cosmopolitune" style="color:inherit">Cosmopolitune</a>
    </h1>

    <h2>While you wait...</h2>
    <h3>You'll have to wait around two seconds for every artist in your playlist that Cosmopolitune's never seen before.</h3>
    <h3>Once your map is ready, you'll be automatically redirected.</h3>
    <p id="mobile_message" style="color: red; font-style: italic; font-size:90%;">This page usually doesn't fully load on mobile. Use desktop!</p>

    <br><br>

    <p>Check out this playlist, it contains at least one song from every country ever seen by Cosmopolitune.</p>
    <p>Find an artist from a new country and it'll be added here!</p>

    <div class="framedRight" style="display:inline-block;"><% out.println(playlistEmbed); %></div>
    <div style="display:inline-block"><br><br></div>
    <div class="framedLeft" id="regions_div" style="display:inline-block;" ></div>

    <div>
        <br><br><br>
        <p>Made by <a href="https://lewisforbes.com/">Lewis Forbes</a></p>
        <p>Code available on <a href="https://github.com/lewisforbes/Cosmopolitune">GitHub</a></p>
    </div>
</center>
</body>
</html>
