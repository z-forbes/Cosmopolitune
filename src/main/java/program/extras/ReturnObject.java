package program.extras;

import program.spotify.GetPlaylistTracks;

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
    private String successMessage;
    public String getSuccessMessage() { return successMessage; }
    /** the JS to be used to display the final map **/
    private String mapJS;
    public String getMapJS() { return mapJS; }
    /** HTML to embed the user's playlist **/
    private String playlistEmbed;
    public String getPlaylistEmbed() { return playlistEmbed; }

    /* UNSUCCESSFUL GENERATION **/
    /** the message to be shown in the case of an error **/
    private String errorMessage;
    public String getErrorMessage() { return errorMessage; }


    /** the class constructor **/
    public ReturnObject() { }


    /** assigns values if program is successful **/
    public void successful(HashMap<String, String> artists, String playlistLink) {
        this.success = true;
        this.successMessage = getSuccessMessage(artists);
        this.mapJS = mkMapJS(artists); // note this removes nulls values from artists
        this.playlistEmbed = mkPlaylistEmbed(playlistLink);
    }

    /** produces the relevant Javascript for use in the .jsp file **/
    private static String mkMapJS(HashMap<String, String> artists) {
        final String MIN_COLOUR = "#8B8CFC";
        final String MAX_COLOUR = "#171AFD";
        final String BG_COLOUR = "#a2e0fc";
        final String DEFAULT_COLOUR = "#dedede";

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
                data.toString() +
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
    private static String codeToName(String code) {
        return new Locale("", code).getDisplayCountry();
    }

    /** produces HTML to embed the user's playlist **/
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


    /** assigns values if the program is unsuccessful **/
    public void unsuccessful(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }
}
