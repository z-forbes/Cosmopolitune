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
        String mapsJS = (String) request.getAttribute("mapsJS");
        out.println(mapsJS);
        %>
    </script>
    <style>
        @media only screen and (max-width: 1000px) {
            .map {
                height: inherit;
                width:95%;
            }

            .hide_on_mobile {
                display: none;
            }
        }


        @media only screen and (min-width: 1001px) {
            .map {
                height: inherit;
                width: 650px
            }
            .map_div {
                display: inline-block;
            }

            .hide_on_desktop {
                display: none;
            }
        }
    </style>
</head>
<body>
<%
String welcomeMessage = (String) request.getAttribute("welcomeMessage");
String newCountriesMessage = (String) request.getAttribute("newCountriesMessage");
String artistsSuccessMessage = (String) request.getAttribute("artistsSuccessMessage");
String albumsSuccessMessage = (String) request.getAttribute("albumsSuccessMessage");
String tracksSuccessMessage = (String) request.getAttribute("tracksSuccessMessage");
String togetherSuccessMessage = (String) request.getAttribute("togetherSuccessMessage");
String usedItems = (String) request.getAttribute("usedItemsStr");
%>
<center>
    <h1>
        <a href="http://cosmopolitune.us-east-2.elasticbeanstalk.com/" style="color:inherit">Cosmopolitune</a>
    </h1>

    <h3><% out.println(welcomeMessage); %></h3>
    <div class="map_div">
        <h3>Followed Artists</h3>
        <div id="artists_div" class="framedFirst map"></div>
        <p><% out.println(artistsSuccessMessage); %></p>
    </div>
    <div class="map_div">
        <h3>Saved Albums</h3>
        <div id="albums_div" class="framedLast map"></div>
        <p><% out.println(albumsSuccessMessage); %></p>
    </div>
    <br class="hide_on_mobile">
    <div class="map_div">
        <h3>Saved Tracks</h3>
        <div id="tracks_div" class="framedFirst map"></div>
        <p><% out.println(tracksSuccessMessage); %></p>
    </div>
    <div class="map_div">
        <h3>All Combined</h3>
        <div id="together_div" class="framedLast map"></div>
        <p><% out.println(togetherSuccessMessage); %></p>
    </div>

    <p><% out.println(newCountriesMessage); %></p>
    <br><br>
    <p>Currently, only the <% out.println(usedItems); %> most recent followed artists, saved albums and saved tracks are considered.</p>
    <br>
    <h3><a href="index.html">Make Another!</a></h3>
    <div class="hide_on_desktop"><br><br></div>
    <div>
        <p>Made by <a href="https://zoeforbes.cc/">Zoe Forbes</a></p>
        <p>Code available on <a href="https://github.com/z-forbes/Cosmopolitune">GitHub</a></p>
    </div>
</center>
</body>
</html>
