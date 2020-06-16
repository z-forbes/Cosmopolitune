package program.playlist;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import program.extras.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used to retrieve a list of tracks from a given Spotify playlist
 */

public class GetPlaylistTracks {

    /** returns a list of tracks in a given playlist **/
    public static ArrayList<Track> getTracks(String playlistURL, boolean getAll) {
        final SpotifyApi api = SpotifyPlaylistAuth.getAPI();
        final String id = URLtoID(playlistURL);

        int offset = 0;
        ArrayList<Track> currentRequest = parseRawData(getPlaylistTracks(api, id, offset));
        if (!getAll) {
            return currentRequest;
        }
        // iff getAll
        ArrayList<Track> fullTracks = new ArrayList<>();
        while (currentRequest.size()>0) {
            fullTracks.addAll(currentRequest);

            offset += 100;
            currentRequest = parseRawData(getPlaylistTracks(api, id, offset));
        }
        return fullTracks;
    }

    /** extracts and returns a playlist ID from playlist URL **/
    public static String URLtoID(String input) {
        final String before = "/playlist/";
        final String after = "?si=";

        if (!input.contains(before)) {
            throw new IllegalArgumentException("Invalid URL");
        }

        String output = input.substring(input.indexOf(before) + before.length());
        if (input.contains(after)) {
            return output.substring(0, output.indexOf(after));
        } else {
         return output;
        }
    }

    /** returns a playlist object from a given playlist ID **/
    private static Paging<PlaylistTrack> getPlaylistTracks(SpotifyApi api, String id, int offset) {
        final GetPlaylistsItemsRequest getPlaylistsItemsRequest = api.getPlaylistsItems(id).offset(offset).build();
        try {
            return getPlaylistsItemsRequest.execute();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Was unable to get the tracks from the given playlist.");
    }

    /** converts a the raw data into to a list of tracks **/
    private static ArrayList<Track> parseRawData(Paging<PlaylistTrack> playlistTracks) {
        ArrayList<Track> output = new ArrayList<Track>();
        for (PlaylistTrack plt : playlistTracks.getItems()) {
            output.add((Track) plt.getTrack());
        }

        return output;
    }
}
