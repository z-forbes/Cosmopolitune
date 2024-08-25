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
        String mapJS = (String) request.getAttribute("playlistMapJS");
        out.println(mapJS);
        %>
    </script>
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
</head>
<body>
<%
String successMessage = (String) request.getAttribute("successMessage");
String playlistEmbed = (String) request.getAttribute("playlistEmbed");
String newCountriesMessage = (String) request.getAttribute("newCountriesMessage");
%>
<center>
    <h1>
        <a href="http://cosmopolitune.us-east-2.elasticbeanstalk.com/" style="color:inherit">Cosmopolitune</a>
    </h1>

    <h3>Here's your map:</h3>
    <p><% out.println(newCountriesMessage); %></p>
    <div class="framedFirst desktop_inline" id="regions_div"></div>
    <div class="hide_on_desktop"><br></div>
    <div class="desktop_inline"><% out.println(playlistEmbed); %> id="playlist" class="framedLast"></iframe></div>


    <p><% out.println(successMessage); %></p>
    <br>
    <h3><a href="index.html">Make Another!</a></h3>
    <br><br>
    <div>
    <p>Made by <a href="https://zoeforbes.cc/">Zoe Forbes</a></p>
    <p>Code available on <a href="https://github.com/z-forbes/Cosmopolitune">GitHub</a></p>
    </div>
</center>
</body>
</html>
