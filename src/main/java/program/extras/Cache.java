package program.extras;

import program.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

/**
 * A cache of all artists and their countries.
 */

public class Cache {
    /** the save/load method chosen **/
    private Main.SaveLoadMethod chosenMethod;
    /** the paths to the cache file **/
    private String loadPath;
    private String savePath;

    /** the string separating fields in each line of the file **/
    private static final String CACHE_FIELD_SEP = "<.>";
    /** the string to represent null data **/
    public static final String NULL_DATA = "[]";
    /** the string to return when no cache entry is found for query **/
    public static final String NO_DATA = "[NO DATA]";

    /** hashmap containing data from the cache **/
    private HashMap<String, String> artistsData = new HashMap<String, String>();
    /** true if the data has been loaded, false otherwise **/
    private boolean dataLoaded;
    /** true if new data has been loaded, used to prevent unnecessary api calls **/
    private boolean newData;


    /** constructor when local cache storage is used **/
    public Cache(Main.SaveLoadMethod chosenMethod, String loadPath, String savePath) {
        this.dataLoaded = false;
        this.newData = false;
        this.chosenMethod = chosenMethod;
        assert chosenMethod != Main.SaveLoadMethod.BEANSTALK;
        assert loadPath != null;
        assert savePath != null;

        this.loadPath = loadPath;
        this.savePath = savePath;
        loadData();
    }

    /** constructor when elastic beanstalk cache storage is used **/
    public Cache(Main.SaveLoadMethod chosenMethod) {
        this.dataLoaded = false;
        this.newData = false;
        this.chosenMethod = chosenMethod;
        assert chosenMethod != Main.SaveLoadMethod.BEANSTALK;
        loadData();
    }


    /** loads data from the cache file **/
    private void loadData() {
        String loadedData = null;
        if (chosenMethod == Main.SaveLoadMethod.TERMINAL) {
            assert loadPath != null;
            loadedData = loadFromFile(loadPath);
        }
        if (chosenMethod == Main.SaveLoadMethod.LOCAL_HOST) {
            assert loadPath != null;
            loadedData = loadFromWeb(loadPath);
        }
        if (chosenMethod == Main.SaveLoadMethod.BEANSTALK ) {
            loadedData = BucketData.loadCache();
        }

        if (loadedData == null) {
            throw new IllegalArgumentException("Was unable to load data from the cache.");
        }

        try {
            String[] splitLine;
            for (String line : loadedData.split("\n")) {
                splitLine = line.split(CACHE_FIELD_SEP);
                assert splitLine.length == 2;
                artistsData.put(splitLine[0], splitLine[1].equals(NULL_DATA) ? null : splitLine[1]);
            }
            dataLoaded = true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Cache formatted incorrectly:\n" + loadedData);
        }
    }

    /** loads data from a file **/
    private String loadFromFile(String path) {
        String output = null;
        try {
            StringBuilder readFile = new StringBuilder();
            Scanner fileReader = new Scanner(new File(path));
            while (fileReader.hasNextLine()) {
                readFile.append(fileReader.nextLine()).append("\n");
            }
            fileReader.close();
            output = readFile.toString();
        } catch (Exception ignored) { }
        return output;
    }

    /** loads data from webpage **/
    private String loadFromWeb(String url) {
        String output = null;
        try {
            URL urlObject = new URL(url);
            StringBuilder readPage = new StringBuilder();
            Scanner pageReader = new Scanner(urlObject.openStream(),"UTF-8");
            while (pageReader.hasNextLine()) {
                readPage.append(pageReader.nextLine()).append("\n");
            }
            pageReader.close();
            output = readPage.toString();
        } catch (Exception ignored) { }
        return output;
    }


    /** saves data to the cache **/
    public void saveData() {
        assert dataLoaded;
        if (!newData) {
            System.out.println("No new data so cache not updated");
            dataLoaded = false; // should not be saved before being loaded again
            return;
        }
        /* formats saved data */
        StringBuilder toSave = new StringBuilder();
        String currentCountry;
        for (String artist : artistsData.keySet()) {
            toSave.append(artist).append(CACHE_FIELD_SEP);
            currentCountry = artistsData.get(artist);
            if (currentCountry == null) {
                toSave.append(NULL_DATA);
            } else {
                toSave.append(currentCountry); // converts code to name if applicable
            }
        toSave.append("\n");
        }

        /* saves the formatted data to file */
        boolean saveSuccess = false;
        if ((chosenMethod == Main.SaveLoadMethod.LOCAL_HOST) || (chosenMethod == Main.SaveLoadMethod.TERMINAL)) {
            // writes to local file
            try {
                OutputStreamWriter writer = new OutputStreamWriter(
                        new FileOutputStream(savePath), StandardCharsets.UTF_8.newEncoder());
                writer.write(toSave.toString());
                writer.close();
                saveSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("\nUnable to save data to cache file.");
            }
        }

        if (chosenMethod == Main.SaveLoadMethod.BEANSTALK) {
            BucketData.saveCache(toSave.toString());
            saveSuccess = true;
        }

        if (!saveSuccess) {
            throw new IllegalArgumentException("Was unable to save the cache.");
        }
        dataLoaded = false; // should not be saved before being loaded again
        newData = false;
    }

    /** queries the cache for a given artist's country. The NO_DATA string is returned if artist not found **/
    public String query(String artist) {
        return artistsData.getOrDefault(artist, NO_DATA);
    }

    /** adds/overwrites an artist and country pair **/
    public void addEntry(String artist, String country) {
        artistsData.put(artist, country);
        newData = true;
    }
}
