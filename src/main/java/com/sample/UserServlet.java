package com.sample;

import program.Main;
import program.extras.ReturnObject;
import program.playlist.NewPlaylistRequest;
import program.user.NewUserRequest;
import program.user.SpotifyUserAuth;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "userservlet",
        urlPatterns = "/user-map"
)
public class UserServlet extends HttpServlet {

    /** the object returned from the Main method **/
    private static ReturnObject returnedData;
    /** true if the waiting page has already been loaded **/
    private static boolean waitPageLoaded = false;
    /** the oAuth code **/
    private static String code;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reset();
        resp.sendRedirect(SpotifyUserAuth.getURL()); // sends user to oAuth page
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (code == null) {
            String fullQuery = "";
            fullQuery += req.getQueryString(); // done like this to avoid NPE
            if (fullQuery.contains("code=")) {
                code = fullQuery.substring(fullQuery.indexOf("=") + 1);
            } else {
                goHome(req, resp);
                return;
            }
        }

        if (!waitPageLoaded) {
            String link = "https://open.spotify.com/playlist/0PJ5WPdsEfvJkxdQEenKFF"; // link to the Cosmopolitune playlist
            returnedData = NewPlaylistRequest.newRequest(link);
            req.setAttribute("playlistMapJS", PlaylistServlet.makeWaitMap(returnedData.getPlaylistMapJS()));
            req.setAttribute("playlistEmbed", returnedData.getPlaylistEmbed());
            if (Main.CHOSEN_METHOD == Main.SaveLoadMethod.BEANSTALK) {
                req.setAttribute("redirect", "/user-map");
            } else {
                req.setAttribute("redirect", "/cosmopolitune/user-map");
            }
            returnedData = null;

            RequestDispatcher view = req.getRequestDispatcher("wait.jsp");
            waitPageLoaded = true;
            view.forward(req,resp);
            return;
        }

        returnedData = NewUserRequest.newRequest(code);

            if (returnedData.getSuccess()) {
                req.setAttribute("newCountriesMessage", returnedData.getNewCountriesMessage());

                req.setAttribute("mapsJS", returnedData.getUserMapsJS());
                req.setAttribute("artistsSuccessMessage", returnedData.getArtistsSuccessMessage());
                req.setAttribute("albumsSuccessMessage", returnedData.getAlbumsSuccessMessage());
                req.setAttribute("tracksSuccessMessage", returnedData.getTracksSuccessMessage());
                req.setAttribute("togetherSuccessMessage", returnedData.getTogetherSuccessMessage());
                req.setAttribute("usedItemsStr", ("" + NewUserRequest.TO_GET));
                String welcomeMessage = "Here're your maps:";
                if (returnedData.getUserName() != null) {
                    welcomeMessage = "Here are the maps for " + returnedData.getUserName() + ":";
                }
                req.setAttribute("welcomeMessage", welcomeMessage);

                RequestDispatcher view = req.getRequestDispatcher("usermaps.jsp");
                reset();
                view.forward(req, resp);
            } else {
                req.setAttribute("errorMessage", returnedData.getErrorMessage());
                RequestDispatcher view = req.getRequestDispatcher("invalid.jsp");
                reset();
                view.forward(req, resp);
            }
    }

    /** redirects the user to the homepage **/
    private static void goHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("gohome.jsp");
        view.forward(req,resp);
    }

    /** resets everything for the next instance **/
    private static void reset() {
        waitPageLoaded = false;
        returnedData = null;
        code = null;
    }
}
