package com.sample;

import program.Main;
import program.Main.SaveLoadMethod;
import program.extras.ReturnObject;
import program.playlist.GetPlaylistTracks;
import program.playlist.NewPlaylistRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wrapper.spotify.model_objects.specification.Track;

import java.io.IOException;

@WebServlet(
        name = "playlistservlet",
        urlPatterns = "/playlist-map"
)
public class PlaylistServlet extends HttpServlet {

    /** the link to the spotify playlist **/
    private static String link;
    /** the object returned from the Main method **/
    private static ReturnObject returnedData;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        returnedData = null;
        link = "https://open.spotify.com/playlist/0PJ5WPdsEfvJkxdQEenKFF"; // link to the Cosmopolitune playlist
        returnedData = NewPlaylistRequest.newRequest(link);

        req.setAttribute("playlistMapJS", makeWaitMap(returnedData.getPlaylistMapJS())); 
        req.setAttribute("playlistEmbed", returnedData.getPlaylistEmbed());
        if (Main.CHOSEN_METHOD == SaveLoadMethod.BEANSTALK) {
            req.setAttribute("redirect", "/playlist-map");
        }
        else {
            req.setAttribute("redirect", "/cosmopolitune/playlist-map");
        }

        link = req.getParameter("link");
        returnedData = null;

        RequestDispatcher view = req.getRequestDispatcher("wait.jsp");
        view.forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (link == null) { // should only be called after data has been set
            goHome(req, resp);
            return;
        }

        if (returnedData == null) { // if it's not null, program.user has refreshed after result
            returnedData = NewPlaylistRequest.newRequest(link);
            link = null;
        }

        if (returnedData.getSuccess()) {
            req.setAttribute("playlistMapJS", returnedData.getPlaylistMapJS());
            req.setAttribute("playlistEmbed", returnedData.getPlaylistEmbed());
            req.setAttribute("successMessage", returnedData.getPlaylistSuccessMessage());
            req.setAttribute("newCountriesMessage", returnedData.getNewCountriesMessage());
            RequestDispatcher view = req.getRequestDispatcher("playlistmap.jsp");
            reset();
            view.forward(req,resp);
        } else {
            req.setAttribute("errorMessage", returnedData.getErrorMessage());
            RequestDispatcher view = req.getRequestDispatcher("invalid.jsp");
            reset();
            view.forward(req,resp);
        }
    }

    /** redirects the program.user to the homepage **/
    private static void goHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("gohome.jsp");
        view.forward(req,resp);
    }

    /** resets everything for the next instance **/
    private static void reset() {
        link = null;
        returnedData = null;
    }

    /** converts a normal map to a map on the waiting page **/
    public static String makeWaitMap(String originalJS) {
        final String newColour = "#7D7FFC";
        String output = originalJS.replaceAll("colors: .*],", "colors: ['" + newColour + "', '" + newColour + "'],");
        return output.replace("options = {", "options = {legend: 'hide', enableRegionInteractivity: 'false', ");
    }
}
