import program.playlist.GetPlaylistTracks;
import java.util.ArrayList;
import com.wrapper.spotify.model_objects.specification.Track;

class HelloWorld {
    public static void main(String[] args) {
        String url = "https://open.spotify.com/playlist/2gJv1xEuwtVre05Ere9sXx?si=f680883b2d4e4230";


        ArrayList<Track> tracks = GetPlaylistTracks.getTracks(url, true);
        System.out.println(tracks);
    }
}