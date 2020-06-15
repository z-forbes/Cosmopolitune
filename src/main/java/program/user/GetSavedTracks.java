package program.user;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.library.GetUsersSavedTracksRequest;

import java.util.ArrayList;

public class GetSavedTracks {
    /** the number of saves albums to get **/
    private static final int toGet = 25; // max 50


    /** returns a list of the program.user's saved albums **/
    public static ArrayList<Track> main(SpotifyApi api) {
        return parseRawData(getRawData(api));
    }

    /** gets raw track data from Spotify **/
    private static Paging<SavedTrack> getRawData(SpotifyApi api) {
        final GetUsersSavedTracksRequest getUsersSavedTracksRequest = api.getUsersSavedTracks().limit(toGet).build();
        try {
            return getUsersSavedTracksRequest.execute();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Was unable to get program.user's saved tracks.");
    }

    /** parses the raw data from the server **/
    private static ArrayList<Track> parseRawData(Paging<SavedTrack> rawData) {
        ArrayList<Track> output = new ArrayList<>();
        for (SavedTrack savedTrack: rawData.getItems()) {
            output.add(savedTrack.getTrack());
        }
        return output;
    }
}
