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

    <style>
        @media only screen and (max-width: 1000px) {
            .hide_on_mobile {
                display: none;
            }

            #playlist {
                width: 80%;
                height: 300px
            }

            #regions_div {
                width: 95%;
                height: inherit;
            }
        }


        @media only screen and (min-width: 1001px) {
            .hide_on_desktop {
                display: none;
            }

            .desktop_inline {
                display: inline-block;
                vertical-align: middle;
            }
        }
    </style>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        <%
        out.println(mapJS);
        %>
    </script>
</head>
<body>
<%
String playlistEmbed = (String) request.getAttribute("playlistEmbed");
%>
<center>
    <h1>
        <a href="http://cosmopolitune.us-east-2.elasticbeanstalk.com/" style="color:inherit">Cosmopolitune</a>
    </h1>
    <h2>While you wait...</h2>
    <h3>You'll have to wait around two seconds for every artist in your playlist that Cosmopolitune's never seen before.</h3>
    <h3>Once your map is ready, you'll be automatically redirected.</h3>

    <br class="hide_on_desktop">
    <p class="hide_on_desktop" style="color:red;">This page usually doesn't fully load on mobile.<br>Use desktop!</p>
    <br><br class="hide_on_mobile">

    <p>Check out this playlist, it contains at least one song from every country ever seen by Cosmopolitune.</p>
    <p>Find an artist from a new country and it'll be added here!</p>
    <div class="desktop_inline"><% out.println(playlistEmbed); %> id="playlist" class="framedFirst"></iframe></div>
    <div class="hide_on_desktop"><br></div>
    <div class="framedLast desktop_inline" id="regions_div"></div>

    <div>
        <br><br><br>
        <p>Made by <a href="https://zoeforbes.cc/">Zoe Forbes</a></p>
        <p>Code available on <a href="https://github.com/z-forbes/Cosmopolitune">GitHub</a></p>
    </div>
</center>
</body>
</html>
