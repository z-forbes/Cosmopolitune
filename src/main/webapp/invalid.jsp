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

    <form method="post" action="playlist-map" autocomplete="off">
        <br>
        <div>
            <label for="link">Enter a link to a Spotify playlist:</label><br><br>
            <input type="text" id="link" name="link" placeholder="Make sure it's public" size="50" required>
        </div>
        <div>
            <br>
            <input type="submit" value="Make Map">
            <p style="color: red; font-style: italic; font-size:90%; "><% out.println(errorMessage); %></p>
        </div>
    </form>
    <form method="post" action="user-map">
        <label for="link">Or make maps based on your library:</label><br><br>
        <input type="submit" value="Make Maps">
    </form>
    <br><br>
    <div style="display: inline-block; vertical-align: top;">
        <p>This playlist contains songs covering every country seen by this app.</p>
        <iframe src="https://open.spotify.com/embed/playlist/0PJ5WPdsEfvJkxdQEenKFF" width="400" height="400" allowtransparency="true" allow="encrypted-media" class="framedRight"></iframe>
        <br>
        <br>
        <form name="submitForm" method="POST" action="playlist-map">
            <input type="hidden" name="link" value="https://open.spotify.com/playlist/0PJ5WPdsEfvJkxdQEenKFF">
            <a href="javascript:document.submitForm.submit()">See its map</a>
        </form>

    </div>
    <div style="display: inline-block; vertical-align: top;">
        <br><br><br>
        <h2 style="font-size:x-large">Thanks to:</h2><br>
        <p><a href="https://github.com/crayarikar">crayarikar</a> for design improvements.</p><br>
        <table style="text-align: center; width:400px;">
            <tr>
                <td><a href="https://github.com/thelinmichael/spotify-web-api-java">thelinmichael</a><br><br></td>
                <td><a href="https://developer.spotify.com/documentation/web-api/">The Spotify Web API</a><br><br></td>
            </tr>
            <tr>
                <td><a href="https://musicbrainz.org/">MusicBrainz</a><br><br></td>
                <td><a href="https://developers.google.com/chart/interactive/docs/gallery/geochart">Google Charts</a><br><br></td>
            </tr>
        </table>
        <p>Members of <a href="https://www.facebook.com/groups/297245530737226/">Guess the Map</a> for their feedback.</p><br><br>
    </div>
    <div>
        <br><br>
        <p>Made by <a href="https://lewisforbes.com/">Lewis Forbes</a></p>
        <p>Code available on <a href="https://github.com/lewisforbes/Cosmopolitune">GitHub</a></p>
    </div>
</center>
</body>

</html>
