package com.sample;

import program.Main;
import program.extras.ReturnObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "mapservlet",
        urlPatterns = "/map"
)
public class MapServlet extends HttpServlet {

    /** the link to the spotify playlist **/
    public static String link;
    /** the object returned from the Main method **/
    public static ReturnObject returnedData;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        returnedData = null;
        link = "https://open.spotify.com/playlist/0PJ5WPdsEfvJkxdQEenKFF"; // link to the Cosmopolitune playlist
        Main.main();
        req.setAttribute("mapJS", updateColours(returnedData.getMapJS()));

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

        if (returnedData == null) {
            Main.main();
            link = null;
        }

        if (returnedData.getSuccess()) {
            req.setAttribute("mapJS", returnedData.getMapJS());
            req.setAttribute("playlistEmbed", returnedData.getPlaylistEmbed());
            req.setAttribute("successMessage", returnedData.getSuccessMessage());
            RequestDispatcher view = req.getRequestDispatcher("map.jsp");
            reset();
            view.forward(req,resp);
        } else {
            req.setAttribute("errorMessage", returnedData.getErrorMessage());
            RequestDispatcher view = req.getRequestDispatcher("invalid.jsp");
            reset();
            view.forward(req,resp);
        }
    }

    /** redirects the user to the homepage **/
    private static void goHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("gohome.jsp");
        view.forward(req,resp);
    }

    /** resets everything for the next instance **/
    private static void reset() {
        link = null;
        returnedData = null;
    }

    /** changes the colour gradient on the given map javascript **/
    private static String updateColours(String originalJS) {
        final String newMin = "#ff9cc3";
        final String newMax = "#ff267d";
        return originalJS.replaceAll("colors: .*],", "colors: ['" + newMin + "', '" + newMax + "'],");
    }
}
