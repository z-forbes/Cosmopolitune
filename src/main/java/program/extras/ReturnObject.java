package program.extras;

import program.Main;
import program.playlist.GetPlaylistTracks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * The data which is returned after the main program runs
 */

public class ReturnObject {
    /** the success of the map generation **/
    private boolean success;
    public boolean getSuccess() { return success;}

    /* SUCCESSFUL GENERATION */
    /** the message to be shown to the used detailing the percentage of artists' countries found **/
    private String playlistSuccessMessage;
    public String getPlaylistSuccessMessage() { return playlistSuccessMessage; }
    /** the message to show to the program.user telling them of new countries they found **/
    private String newCountriesMessage;
    public String getNewCountriesMessage() { return newCountriesMessage; }

    /** the JS to be used to display the playlist final map **/
    private String playlistMapJS;
    public String getPlaylistMapJS() { return playlistMapJS; }
    /** HTML to embed the program.user's playlist **/
    private String playlistEmbed;
    public String getPlaylistEmbed() { return playlistEmbed; }

    /** the JS for the generated map **/
    private String userMapsJS;
    public String getUserMapsJS() { return userMapsJS; }
    /** the different relevant success rates **/
    private String artistsSuccessMessage;
    private String albumsSuccessMessage;
    private String tracksSuccessMessage;
    private String togetherSuccessMessage;
    public String getArtistsSuccessMessage() { return artistsSuccessMessage; }
    public String getAlbumsSuccessMessage() { return albumsSuccessMessage; }
    public String getTracksSuccessMessage() { return tracksSuccessMessage; }
    public String getTogetherSuccessMessage() { return togetherSuccessMessage; }
    /** the name of the user **/
    private String userName;
    public String getUserName() { return userName; }


    /* UNSUCCESSFUL GENERATION **/
    /** the message to be shown in the case of an error **/
    private String errorMessage;
    public String getErrorMessage() { return errorMessage; }


    /** the class constructor **/
    public ReturnObject() { }


    /** assigns values if playlist request is successful **/
    public void playlistSuccessful(HashMap<String, String> playlistArtists, String playlistLink, String newCountriesMessage) {
        this.success = true;
        this.playlistSuccessMessage = getSuccessMessage(playlistArtists);
        this.playlistMapJS = mkMapJS(playlistArtists); // note this removes nulls values from artists
        this.playlistEmbed = mkPlaylistEmbed(playlistLink);
        this.newCountriesMessage = newCountriesMessage;
    }

    /** assigns values if program.user request is successful **/
    public void userSuccessful(HashMap<String, String> artistsArtists, HashMap<String, String> albumsArtists,
                               HashMap<String, String> tracksArtists, String newCountriesMessage, String userName) {
        HashMap<String, String> artistsTogether = Main.combineHashMaps(Main.combineHashMaps(artistsArtists, albumsArtists), tracksArtists);
        this.userMapsJS = mkMapsJS(artistsArtists, albumsArtists, tracksArtists, artistsTogether);

        this.success = true;
        this.artistsSuccessMessage = getSuccessMessage(artistsArtists);
        this.albumsSuccessMessage = getSuccessMessage(albumsArtists);
        this.tracksSuccessMessage = getSuccessMessage(tracksArtists);
        this.togetherSuccessMessage = getSuccessMessage(artistsTogether);

        this.newCountriesMessage = newCountriesMessage;
        this.userName = userName;
    }

    /** assigns values if the program is unsuccessful **/
    public void unsuccessful(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }


    /** produces the relevant Javascript for use in the .jsp file for a single map **/
    private static String mkMapJS(HashMap<String, String> artists) {
        final String MIN_COLOUR = "#8B8CFC";
        final String MAX_COLOUR = "#171AFD";
        final String BG_COLOUR = "#a2e0fc";
        final String DEFAULT_COLOUR = "#dedede";



        final String API_KEY = Confidential.ReturnObject_API_KEY;

        String output = "      google.charts.load('current', {\n" +
                "        'packages':['geochart'],\n" +
                "        // Note: you will need to get a mapsApiKey for your project.\n" +
                "        // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings\n" +
                "        'mapsApiKey': '" + API_KEY + "'\n" +
                "      });\n" +
                "      google.charts.setOnLoadCallback(drawRegionsMap);\n" +
                "\n" +
                "      function drawRegionsMap() {\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
                mkData(artists) +
                "        ]);\n" +
                "\n" +
                "        var options = " +
                    "{minValue: 0," +
                    "colors: ['" + MIN_COLOUR + "', '" + MAX_COLOUR + "']," +
                    "backgroundColor: '" + BG_COLOUR + "'," +
                    "datalessRegionColor: '" + DEFAULT_COLOUR + "'," +
                    "defaultColor: '" + DEFAULT_COLOUR + "',};" +
                "\n" +
                "        var chart = new google.visualization.GeoChart(document.getElementById('regions_div'));\n" +
                "\n" +
                "        chart.draw(data, options);\n" +
                "      }";
        return output;
    }

    /** produces the relevant Javascript for three maps **/
    private static String mkMapsJS(HashMap<String, String> artistsArtists, HashMap<String, String> albumsArtists, HashMap<String, String> tracksArtists, HashMap<String, String> artistsTogether) {
        final String API_KEY = Confidential.ReturnObject_API_KEY;
        final String MIN_COLOUR = "#8B8CFC";
        final String MAX_COLOUR = "#171AFD";
        final String BG_COLOUR = "#a2e0fc";
        final String DEFAULT_COLOUR = "#dedede";

        String output =
                "      google.charts.load('current', {'packages':['geochart'],'mapsApiKey': '" + API_KEY + "'});\n" +
                "\n" +
                "      google.charts.setOnLoadCallback(drawArtistsMap);\n" +
                "      google.charts.setOnLoadCallback(drawAlbumsMap);\n" +
                "      google.charts.setOnLoadCallback(drawTracksMap);\n" +
                "      google.charts.setOnLoadCallback(drawTogetherMap);\n" +
                "\n" +
                "      function drawArtistsMap() {\n" +
                "\n" +
                        "        var data = google.visualization.arrayToDataTable([\n" +
                        mkData(artistsArtists) +
                        "        ]);\n" +
                        "\n" +
                        "        var options = " +
                        "{minValue: 0," +
                        "colors: ['" + MIN_COLOUR + "', '" + MAX_COLOUR + "']," +
                        "backgroundColor: '" + BG_COLOUR + "'," +
                        "datalessRegionColor: '" + DEFAULT_COLOUR + "'," +
                        "defaultColor: '" + DEFAULT_COLOUR + "',};" +

                        "\n" +
                "        var chart = new google.visualization.GeoChart(document.getElementById('artists_div'));\n" +
                "        chart.draw(data, options);\n" +
                "      }\n" +
                "\n" +
                        "      function drawAlbumsMap() {\n" +
                        "\n" +
                        "        var data = google.visualization.arrayToDataTable([\n" +
                        mkData(albumsArtists) +
                        "        ]);\n" +
                        "\n" +
                        "        var options = " +
                        "{minValue: 0," +
                        "colors: ['" + MIN_COLOUR + "', '" + MAX_COLOUR + "']," +
                        "backgroundColor: '" + BG_COLOUR + "'," +
                        "datalessRegionColor: '" + DEFAULT_COLOUR + "'," +
                        "defaultColor: '" + DEFAULT_COLOUR + "',};" +

                        "\n" +
                        "        var chart = new google.visualization.GeoChart(document.getElementById('albums_div'));\n" +
                        "        chart.draw(data, options);\n" +
                        "      }\n" +
                        "\n" +
                        "      function drawTracksMap() {\n" +
                        "\n" +
                        "        var data = google.visualization.arrayToDataTable([\n" +
                        mkData(tracksArtists) +
                        "        ]);\n" +
                        "\n" +
                        "        var options = " +
                        "{minValue: 0," +
                        "colors: ['" + MIN_COLOUR + "', '" + MAX_COLOUR + "']," +
                        "backgroundColor: '" + BG_COLOUR + "'," +
                        "datalessRegionColor: '" + DEFAULT_COLOUR + "'," +
                        "defaultColor: '" + DEFAULT_COLOUR + "',};" +

                        "\n" +
                        "        var chart = new google.visualization.GeoChart(document.getElementById('tracks_div'));\n" +
                        "        chart.draw(data, options);\n" +
                        "      }\n" +
                        "\n" +
                        "      function drawTogetherMap() {\n" +
                        "\n" +
                        "        var data = google.visualization.arrayToDataTable([\n" +
                        mkData(artistsTogether) +
                        "        ]);\n" +
                        "\n" +
                        "        var options = " +
                        "{minValue: 0," +
                        "colors: ['" + MIN_COLOUR + "', '" + MAX_COLOUR + "']," +
                        "backgroundColor: '" + BG_COLOUR + "'," +
                        "datalessRegionColor: '" + DEFAULT_COLOUR + "'," +
                        "defaultColor: '" + DEFAULT_COLOUR + "',};" +

                        "\n" +
                        "        var chart = new google.visualization.GeoChart(document.getElementById('together_div'));\n" +
                        "        chart.draw(data, options);\n" +
                        "      }";
        return output;
    }

    /** creates the data required for the map JS **/
    private static String mkData(HashMap<String, String> artists) {
        removeNullValues(artists);

        // makes a Hashmap of unique countries and their frequency
        HashMap<String, Integer> countriesFreq = new HashMap<String, Integer>();
        int count;
        for (String countryCode : artists.values()) {
            count = countriesFreq.getOrDefault(countryCode, 0) +1;
            countriesFreq.put(countryCode, count);
        }

        StringBuilder data = new StringBuilder("['Country', 'Popularity']");
        for (String country : countriesFreq.keySet()) {
            data.append(",\n['").append(codeToName(country)).append("', ").append(countriesFreq.get(country)).append("]");
        }
        return data.toString();
    }


    /** removes entries with null values in a hashmap **/
    private static void removeNullValues(HashMap<String, String> input) {
        ArrayList<String> keysToRemove = new ArrayList<>();

        for (String key : input.keySet()) {
            if (input.get(key) == null) {
                keysToRemove.add(key);
            }
        }

        for (String key : keysToRemove) {
            input.remove(key);
        }
    }

    /** converts a country code into a country name **/
    public static String codeToName(String code) {
        return new Locale("", code).getDisplayCountry();
    }

    /** produces HTML to embed the program.user's playlist **/
    private static String mkPlaylistEmbed(String url) {
        final String id = GetPlaylistTracks.URLtoID(url);
        return "<iframe src=\"https://open.spotify.com/embed/playlist/" + id + "\" width=\"300\" height=\"380\" frameborder=\"0\" allowtransparency=\"true\" allow=\"encrypted-media\"></iframe>";
    }

    /** generates and returns the successMessage string **/
    private static String getSuccessMessage(HashMap<String, String> artists) {
        int nonNull = 0;
        for (String country : artists.values()) {
            if (country != null) {
                nonNull++;
            }
        }
        int successRate = Math.round(100 * (float) nonNull / (float) artists.size());
        return "Found countries for " + successRate + "% of the artists.";
    }
}
