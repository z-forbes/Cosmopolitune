package program;

import com.sample.MapServlet;
import program.countries.CountryFromPlace;
import program.countries.GetLocation;
import program.extras.Cache;
import program.extras.Model;
import program.extras.ReturnObject;
import program.spotify.GetArtists;
import program.spotify.GetPlaylistTracks;
import program.spotify.SpotifyAuth;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Track;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The main class for the program.
 * Produces relevant javascript to display a map of the artists in a given Spotify playlist.
 */

public class Main {

    /** the different methods for saving and loading the cache **/
    public enum SaveLoadMethod {BEANSTALK, LOCAL_HOST, TERMINAL}
    private static final SaveLoadMethod CHOSEN_METHOD = SaveLoadMethod.BEANSTALK; // unless testing, use BEANSTALK

    /** the maximum number of artists allowed in a playlist **/
    private static final int MAX_ARTISTS = 150;

    /** the cache of artists and their countries **/
    private static Cache cache = initialiseCache();
    /** creates a new cache object and returns it **/
    private static Cache initialiseCache() {
        if (CHOSEN_METHOD == SaveLoadMethod.BEANSTALK) {
            return new Cache(SaveLoadMethod.BEANSTALK);
        }

        String loadPath, savePath;
        if (CHOSEN_METHOD == SaveLoadMethod.LOCAL_HOST) {
            loadPath = "http://localhost:8080/cosmopolitune/testCache.txt";
            savePath = "src/main/webapp/testCache.txt";
            return new Cache(CHOSEN_METHOD, loadPath, savePath);
        }

        if (CHOSEN_METHOD == SaveLoadMethod.TERMINAL) {
           loadPath = "src\\main\\webapp\\testCache.txt";
           savePath = loadPath;
           return new Cache(CHOSEN_METHOD, loadPath, savePath);
        }

        throw new IllegalArgumentException("Invalid save/load method attempted: " + CHOSEN_METHOD.name());
    }

    public static void main(String[] args) {
        final String playlistURL = "https://open.spotify.com/playlist/4KUehPRtELUWsYaS2tJN0k?si=gk8YhP-tQtWaRxB9lHZ2Pg";
        SpotifyApi api = SpotifyAuth.getAPI();
        ArrayList<Track> tracks = GetPlaylistTracks.getTracks(api, playlistURL);
        System.out.println(tracks.size());
    }

    /** the main method for the entire program **/
    public static void main() {
        String playlistURL = MapServlet.link;
        assert playlistURL != null;

        ReturnObject returnObject = new ReturnObject();

        try {
            SpotifyApi api = SpotifyAuth.getAPI();
            ArrayList<Track> tracks = GetPlaylistTracks.getTracks(api, playlistURL);
            HashMap<String, String> artists = GetArtists.getArtists(tracks);
            if ((!playlistURL.contains(Model.modelID)) && (artists.size() > MAX_ARTISTS)) { // Cosmopolitune playlist can have as many as it wants.
                throw new IllegalArgumentException("Maximum different artists allowed is " + MAX_ARTISTS + ", you have " + artists.size() + ".");
            }
            addCountries(artists);
            String newCountriesMessage = Model.updateModel(artists, playlistURL);
            printHM(artists);
            cache.saveData();

            returnObject.successful(artists, playlistURL, newCountriesMessage); // sets values to return
            MapServlet.returnedData = returnObject;
        } catch (Exception e) {
            cache.saveData();
            e.printStackTrace();
            returnObject.unsuccessful(e.getMessage()); // sets values to return
            MapServlet.returnedData = returnObject;
        }
    }

    /** populates the artists hashmap with each artist's country **/
    private static void addCountries(HashMap<String, String> artists) {
        String currentCountry;
        for (String artist : artists.keySet()) {
            currentCountry = cache.query(artist);
            if ((currentCountry == null) || (!currentCountry.equals(Cache.NO_DATA))) {
                artists.put(artist, currentCountry);
                continue;
            }

            currentCountry = CountryFromPlace.getCountryCode(GetLocation.getLoc(artist));
            System.out.println("Country for " + artist + ": " + currentCountry);
            artists.put(artist, currentCountry);
            cache.addEntry(artist, currentCountry);
        }
    }

    /** pretty prints a hashmap **/
    private static void printHM(HashMap<String, String> input) {
        for (String key : input.keySet()) {
            System.out.print("Key: " + key + " | ");
            System.out.println("Value: " + input.get(key));
        }
    }
}
