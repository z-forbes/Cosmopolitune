package program.extras;

/**
 * Holds confidential data for the program
 * Should NOT be made public!
 */

public class Confidential {
    /* AWS Keys */ // these aren't used when self-hosted
    public static final String BucketData_accessKey = "";
    public static final String BucketData_secretKey = "";

    /* Spotify Web API keys */
    public static final String SpotifyAuth_refresh = "";
    public static final String SpotifyAuth_clientID = "";
    public static final String SpotifyAuth_clientSecret = "";

    /* Google Maps Platform Keys */
    // Geocoding Key
    public static final String CountryFromPlace_API_KEY = "";
    // GeoCharts
    public static final String ReturnObject_API_KEY = "";
}
