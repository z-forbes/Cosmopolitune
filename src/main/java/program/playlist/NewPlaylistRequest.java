package program.playlist;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import program.Main;
import program.extras.Model;
import program.extras.ReturnObject;

import java.util.ArrayList;

public class NewPlaylistRequest {
    /** the main method used for making a new request for a playlist **/
    public static ReturnObject newRequest(String playlistURL) {
        try {
            ArrayList<Track> tracks = GetPlaylistTracks.getTracks(playlistURL, playlistURL.contains(Model.modelID));
            ArrayList<String> artistsNames = artistsFromTracks(tracks);
            return Main.playlistMain(artistsNames, playlistURL);
        } catch (Exception e) {
            ReturnObject failed = new ReturnObject();
            failed.unsuccessful("Error, please try again.");
            return failed;
        }
    }

    /** makes a list of all artists' names from a list of tracks **/
    public static ArrayList<String> artistsFromTracks(ArrayList<Track> tracks) {
        ArrayList<String> artists = new ArrayList<String>();
        for (Track track : tracks) {
            if (track == null) {
                continue;
            }
            appendArtist(artists, track.getArtists());
        }
        return artists;
    }

    /** appends the names of artists in a given array to a given list **/
    public static void appendArtist(ArrayList<String> currentAL, ArtistSimplified[] toAdd) {
        for (ArtistSimplified nextArtist : toAdd) {
            currentAL.add(nextArtist.getName());
        }
    }
}
