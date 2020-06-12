<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="stylesheet.css">
    <title>Cosmopolitune</title>
</head>

<body>
<%
String errorMessage = (String) request.getAttribute("errorMessage");
%>
<center>
    <h1>
        <a href="http://lewis-forbes.us-east-2.elasticbeanstalk.com/cosmopolitune" style="color:inherit">Cosmopolitune</a>
    </h1>
    <h3>
        See how diverse your playlists are!
    </h3>

    <form method="post" action="map" autocomplete="off">
        <br>
        <div>
            <label for="link">Enter a link to a Spotify playlist:</label><br><br>
            <input type ="text" id="link" name="link" placeholder="Make sure it's public" size="30">
        </div>
        <div>
            <br>
            <input type="submit" value="Make Map">
            <p style="color: red; font-style: italic; font-size:90%; "><% out.println(errorMessage); %></p>
        </div>
        <br>
    </form>
    <br><br>
    <h2 style="font-size:x-large">Hefty merci to:</h2>
    <a href="https://github.com/thelinmichael/spotify-web-api-java">thelinmichael on Github</a><br><br>
    <a href="https://musicbrainz.org/">MusicBrainz</a>
    <div>
        <br><br>
        <p>Made by <a href="https://lewisforbes.com/">Lewis Forbes</a></p>
        <p>Code available on <a href="https://github.com/lewisforbes/Cosmopolitune">GitHub</a></p>
    </div>
</center>
</body>

</html>