package program;

import program.countries.CountryFromPlace;
import program.countries.GetLocation;
import program.extras.Cache;
import program.extras.Model;
import program.extras.ReturnObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The main class for the program.
 * Produces relevant javascript to display a map of the artists in a given Spotify playlist.
 */

public class Main {

    /** the different methods for saving and loading the cache **/
    public enum SaveLoadMethod {BEANSTALK, LOCAL_HOST, TERMINAL}
    public static final SaveLoadMethod CHOSEN_METHOD = SaveLoadMethod.BEANSTALK; // unless testing, use BEANSTALK

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

    /** main method for playlist requests **/
    public static ReturnObject playlistMain(ArrayList<String> artistNames, String playlistURL) {
        ReturnObject returnObject = new ReturnObject();
        try {
            HashMap<String, String> artists = initialiseArtists(artistNames);
            if ((!playlistURL.contains(Model.modelID)) && (artists.size() > MAX_ARTISTS)) { // Cosmopolitune playlist can have as many as it wants.
                throw new IllegalArgumentException("Maximum different artists allowed is " + MAX_ARTISTS + ", you have " + artists.size() + ".");
            }
            addCountries(artists);

            String newCountriesMessage = Model.updateModel(artists, playlistURL);
            cache.saveData();
            returnObject.playlistSuccessful(artists, playlistURL, newCountriesMessage); // sets values to return
            return returnObject;
        } catch (Exception e) {
            cache.saveData();
            e.printStackTrace();
            returnObject.unsuccessful(e.getMessage()); // sets values to return
            return returnObject;
        }
    }

    /** main method for program.user requests **/
    public static ReturnObject userMain(
            ArrayList<String> artistsArtistsNames, ArrayList<String> albumArtistsNames, ArrayList<String> tracksArtistsNames, String userName) {
        ReturnObject returnObject = new ReturnObject();
        try {
            HashMap<String, String> artistsArtists = initialiseArtists(artistsArtistsNames);
            HashMap<String, String> albumArtists = initialiseArtists(albumArtistsNames);
            HashMap<String, String> tracksArtists = initialiseArtists(tracksArtistsNames);


            addCountries(artistsArtists);
            addCountries(albumArtists);
            addCountries(tracksArtists);


            String newCountriesMessage = Model.updateModel(combineHashMaps(combineHashMaps(artistsArtists, albumArtists), tracksArtists), ""); // not great but works lol
            cache.saveData();

            returnObject.userSuccessful(artistsArtists, albumArtists, tracksArtists, newCountriesMessage, userName);

            return returnObject;
        } catch (Exception e) {
            cache.saveData();
            e.printStackTrace();
            returnObject.unsuccessful(e.getMessage()); // sets values to return
            return returnObject;
        }
    }

    /** makes a hashmap of unique artists as keys with null values **/
    private static HashMap<String, String> initialiseArtists(ArrayList<String> artistNames) {
        HashMap<String, String> artists = new HashMap<String, String>();
        for (String name : artistNames) {
            if (!artists.containsKey(name)) {
                artists.put(name, null);
            }
        }

        return artists;
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


    /** combines three HashMaps, assuming all matching keys have the same values **/
    public static HashMap<String, String> combineHashMaps(HashMap<String, String> hm1, HashMap<String, String> hm2) {
        HashMap<String, String> combined = new HashMap<>();
        // clones hm1
        for (String key : hm1.keySet()) {
            combined.put(key, hm1.get(key));
        }

        for (String key : hm2.keySet()) {
            combined.putIfAbsent(key, hm2.get(key));
        }
        return combined;
    }
}
