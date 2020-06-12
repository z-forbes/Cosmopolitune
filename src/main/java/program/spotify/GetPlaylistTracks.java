package program.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used to retrieve a list of tracks from a given Program.Spotify playlist
 */

public class GetPlaylistTracks {

    /** the main method for the class **/
    public static ArrayList<Track> getTracks(SpotifyApi api, String url) {
        Playlist playlist = getPlaylist(api, URLtoID(url));
        return playlistToTracks(playlist);
    }

    /** extracts and returns a playlist ID from playlist URL **/
    private static String URLtoID(String input) {
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
    private static Playlist getPlaylist(SpotifyApi api, String id) {
        GetPlaylistRequest getPlaylistRequest = api.getPlaylist(id).build();
        try {
            final Playlist playlist = getPlaylistRequest.execute();

            System.out.println("Successfully received tracks from: " + playlist.getName() + "\n");
            return playlist;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Was unable to get the tracks from the given playlist.");
    }

    /** converts a playlist object to a list of its tracks **/
    private static ArrayList<Track> playlistToTracks(Playlist playlist) {
        ArrayList<PlaylistTrack> playlistTracks =
                new ArrayList<PlaylistTrack>(Arrays.asList(playlist.getTracks().getItems()));

        ArrayList<Track> output = new ArrayList<Track>();
        for (PlaylistTrack plt : playlistTracks) {
            output.add((Track) plt.getTrack());
        }

        return output;
    }
}
