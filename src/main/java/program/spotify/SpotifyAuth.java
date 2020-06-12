package program.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import program.extras.Confidential;

import java.net.URI;
import java.util.Scanner;

/**
 * Class deals with authorization with the Spotify API
 */

public class SpotifyAuth {

    /** the refresh token to make new requests **/
    private static final String refresh = Confidential.SpotifyAuth_refresh;

    /** app-specific API configurations **/
    private static final String clientID = Confidential.SpotifyAuth_clientID;
    private static final String clientSecret = Confidential.SpotifyAuth_clientSecret;
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://www.google.co.uk/");
    private static final String scope = "playlist-modify-public";

    /** the API object to be created and passed through **/
    private static SpotifyApi api = new SpotifyApi.Builder().setClientId(clientID).setClientSecret(clientSecret).setRedirectUri(redirectUri).build();


    /** sets a refreshed access token and returns a SpotifyAPI object of predetermined scope **/
    public static SpotifyApi getAPI() {
        api.setAccessToken(getAccess());
        return api;
    }

    /** refreshes the access token and returns it **/
    private static String getAccess() {
        api.setRefreshToken(refresh);
        final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = api.authorizationCodeRefresh().build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            return authorizationCodeCredentials.getAccessToken();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Unable to refresh and return access token.");
    }


    /** generates a new refresh token when called **/
    private static void getNewRefresh() {
        Scanner temp = new Scanner(System.in);
        System.out.print("Press enter to get a new refresh token...");
        temp.nextLine();

        makeNewCreds(mkCode());
        System.out.println("Refresh token: " + api.getRefreshToken());
    }

    /** gets the user to enter the oAuth code **/
    private static String mkCode() {
        System.out.println("Click the following link:");
        printURI(api);
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the URL you were redirected to\n");
        String input = in.nextLine();
        return input.substring(input.indexOf('=')+1);
    }

    /** prints the oAuth URL **/
    private static void printURI(SpotifyApi api) {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri()
                .scope(scope)
                .show_dialog(false)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        System.out.println("URI: " + uri.toString());
    }

    /** calls the API to set new tokens **/
    private static void makeNewCreds(String codeFromURI) {
        AuthorizationCodeRequest authorizationCodeRequest = api.authorizationCode(codeFromURI).build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            api.setAccessToken(authorizationCodeCredentials.getAccessToken());
            api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            return;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Was unable set credentials.");
    }
}