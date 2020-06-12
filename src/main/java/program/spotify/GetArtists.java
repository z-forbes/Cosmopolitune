package program.spotify;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Makes a HashMap of with all artists in a given list of tracks as keys, and with null values.
 */

public class GetArtists {
    /** the main method for the class **/
    public static HashMap<String, String> getArtists(ArrayList<Track> tracks) {
        // makes list of all artists
        ArrayList<String> artists = new ArrayList<String>();
        for (Track track : tracks) {
            appendArtist(artists, track.getArtists());
        }

        // makes a hashmap of unique artists as keys with null values
        HashMap<String, String> output = new HashMap<String, String>();
        for (String artist : artists) {
            if (!output.containsKey(artist)) {
                output.put(artist, null);
            }
        }

        return output;
    }

    /** appends the names of artists in a given array to a given list **/
    private static void appendArtist(ArrayList<String> currentAL, ArtistSimplified[] toAdd) {
        for (ArtistSimplified nextArtist : toAdd) {
            currentAL.add(nextArtist.getName());
        }
    }
}
