package program.user;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import program.Main;
import program.extras.ReturnObject;

import java.util.ArrayList;

import static program.playlist.NewPlaylistRequest.appendArtist;

/**
 * Created a Return Object for a request based on the user's library
 */

public class NewUserRequest {
    /** the number of songs to get for each map **/
    public static final int TO_GET = 50; // max is 50 due to API limitations


    /** the main method for the class **/
    public static ReturnObject newRequest(String code) {
        SpotifyApi api = SpotifyUserAuth.getAPI(code);
        ArrayList<Artist> followedArtists = GetFollowedArtists.main(api);
        ArrayList<Album> savedAlbums = GetSavedAlbums.main(api);
        ArrayList<Track> savedTracks = GetSavedTracks.main(api);

        String userName = getUserName(api);
        return Main.userMain(artistsToArtistsNames(followedArtists), albumsToArtistsNames(savedAlbums), tracksToArtistsNames(savedTracks), userName);
    }

    /** turn lists of different Spotify objects into lists of their artists; names **/
    private static ArrayList<String> artistsToArtistsNames(ArrayList<Artist> artists) {
        ArrayList<String> artistsNames = new ArrayList<String>();
        for (Artist artist : artists) {
            artistsNames.add(artist.getName());
        }
        return artistsNames;
    }
    private static ArrayList<String> albumsToArtistsNames(ArrayList<Album> albums) {
        ArrayList<String> artistsNames = new ArrayList<String>();
        for (Album album : albums) {
            appendArtist(artistsNames, album.getArtists());
        }
        return artistsNames;
    }
    private static ArrayList<String> tracksToArtistsNames(ArrayList<Track> tracks) {
        ArrayList<String> artistsNames = new ArrayList<String>();
        for (Track track : tracks) {
            appendArtist(artistsNames, track.getArtists());
        }
        return artistsNames;
    }

    /** returns the user's name **/
    private static String getUserName(SpotifyApi api) {
        final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = api.getCurrentUsersProfile().build();
        try {
            final User user = getCurrentUsersProfileRequest.execute();
            return user.getDisplayName();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException("Was unable to get the user's name.");
        }
    }

}
