package program.extras;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import com.wrapper.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.ChangePlaylistsDetailsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import program.spotify.SpotifyAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {
    /** the ID of the model playlist **/
    public static final String modelID = "0PJ5WPdsEfvJkxdQEenKFF";
    /** data from the model playlist **/
    public static HashMap<String, String> modelArtists;

    /** updates the model playlist if applicable. Returns message detailing new countries found **/
    public static String updateModel(HashMap<String, String> artists, String url) {
        if (url.contains(modelID)) {
            modelArtists = artists;
            return "";
        }

        ArrayList<String> newArtists = new ArrayList<>();
        String newCountry;
        ArrayList<String> newCountries = new ArrayList<>();
        for (String artist : artists.keySet()) {
            newCountry = artists.get(artist);
            if (newCountry == null) { continue; }

            if (!modelArtists.containsValue(newCountry)) {
                System.out.println("New country: " + newCountry);
                newArtists.add(artist);
                modelArtists.put(artist, newCountry);
                newCountries.add(ReturnObject.codeToName(newCountry));
            }
        }

        addNewArtists(newArtists);
        return mkAddedMessage(newCountries);
    }

    /** adds new artists' songs to the playlist and updates its description **/
    private static void addNewArtists(ArrayList<String> newArtists) {
        if (newArtists.size() == 0) {
            return;
        }
        SpotifyApi api = SpotifyAuth.getAPI();
        Artist currentArtist;
        Track currentTrack;
        for (String artist : newArtists) {
            currentArtist = getArtist(artist, api);
            currentTrack = getTopTrack(currentArtist, api);
            addToPlaylist(currentTrack, modelID, api);
            final String newDesc = modelArtists.size() + " countries and counting! Find how diverse your playlists are at bit.ly/cosmopolitune.";
            updateDescription(newDesc, modelID, api);
            System.out.println("Added " + currentTrack.getName() + " to Cosmopolitune playlist.");
        }
    }

    /** searches for the provided artist **/
    private static Artist getArtist(String name, SpotifyApi api) {
        final SearchArtistsRequest searchArtistsRequest = api.searchArtists(name).limit(3).build();
        try {
            final Paging<Artist> artistPaging = searchArtistsRequest.execute();
            for (Artist artist : artistPaging.getItems()) {
                if (artist.getName().equalsIgnoreCase(name)) {
                    return artist;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Was unable to find artist: " + name);
        return null;
    }

    /** returns the top track for a provided artist **/
    private static Track getTopTrack(Artist artist, SpotifyApi api) {
        final GetArtistsTopTracksRequest getArtistsTopTracksRequest = api
                .getArtistsTopTracks(artist.getId(), CountryCode.GB)
                .build();
        try {
            final Track[] tracks = getArtistsTopTracksRequest.execute();
            return tracks[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Was unable to find tracks for: " + artist.getName());
        return null;
    }

    /** adds the provided track to the provided playlist **/
    private static void addToPlaylist(Track toAdd, String playlistID, SpotifyApi api) {
        final String[] uris = new String[] {toAdd.getUri()};
        final AddItemsToPlaylistRequest addItemsToPlaylistRequest = api.addItemsToPlaylist(playlistID, uris).build();
        try {
            final SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** changes the description for the given playlist to the given value **/
    private static void updateDescription(String newDesc, String playlistID, SpotifyApi api) {
        final ChangePlaylistsDetailsRequest changePlaylistsDetailsRequest = api.changePlaylistsDetails(playlistID).description(newDesc).build();
        try {
            changePlaylistsDetailsRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** makes a messaging telling the user that they found new countries **/
    private static String mkAddedMessage(ArrayList<String> newCountries) {
        if (newCountries.size() == 0) { return ""; }

        String message = "You were the first person to find ";
        if (newCountries.size() == 1) {
            return message + "a song from " + newCountries.get(0) + "!";
        }

        StringBuilder countryList = new StringBuilder(newCountries.get(0));
        for (int i=1; i<newCountries.size()-1; i++) {
            countryList.append(", ").append(newCountries.get(i));
        }
        countryList.append(" and ").append(newCountries.get(newCountries.size()-1));
        return message + "songs from " + countryList.toString() + "!";
    }
}
