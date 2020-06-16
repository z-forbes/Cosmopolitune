package program.user;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.SavedAlbum;
import com.wrapper.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;

import java.util.ArrayList;

public class GetSavedAlbums {
    /** the number of saves albums to get **/
    private static final int toGet = NewUserRequest.TO_GET;


    /** returns a list of the program.user's saved albums **/
    public static ArrayList<Album> main(SpotifyApi api) {
        return parseRawData(getRawData(api));
    }

    /** gets raw album data from Spotify **/
    private static Paging<SavedAlbum> getRawData(SpotifyApi api) {
        final GetCurrentUsersSavedAlbumsRequest getCurrentUsersSavedAlbumsRequest = api.getCurrentUsersSavedAlbums().limit(toGet).build();
        try {
            return getCurrentUsersSavedAlbumsRequest.execute();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Was unable to get program.user's albums.");
    }

    /** parses the raw data from the server **/
    private static ArrayList<Album> parseRawData(Paging<SavedAlbum> rawData) {
        ArrayList<Album> output = new ArrayList<>();
        for (SavedAlbum savedAlbum : rawData.getItems()) {
            output.add(savedAlbum.getAlbum());
        }
        return output;
    }
}
