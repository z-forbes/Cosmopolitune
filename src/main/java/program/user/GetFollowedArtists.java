package program.user;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;

import java.util.ArrayList;
import java.util.Arrays;

public class GetFollowedArtists {
    /** the number of saves albums to get **/
    private static final int toGet = 25; // max 50


    /** returns a list of the program.user's saved albums **/
    public static ArrayList<Artist> main(SpotifyApi api) {
        return parseRawData(getRawData(api));
    }

    /** gets raw followed artists data from Spotify **/
    private static PagingCursorbased<Artist> getRawData(SpotifyApi api) {
        final GetUsersFollowedArtistsRequest getUsersFollowedArtistsRequest = api.getUsersFollowedArtists(ModelObjectType.ARTIST).limit(toGet).build();
        try {
            return getUsersFollowedArtistsRequest.execute();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Was unable to get program.user's followed artists.");
    }

    /** parses the raw data from the server **/
    private static ArrayList<Artist> parseRawData(PagingCursorbased<Artist> rawData) {
        return new ArrayList<>(Arrays.asList(rawData.getItems()));
    }
}
