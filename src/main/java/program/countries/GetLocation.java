package program.countries;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URL;

/**
 * Used to find the location of a given artist.
 */

public class GetLocation {

    /** the number of artists to check a match against **/
    private static final int ARTISTS_TO_CHECK = 3;

    /** the index of the matched artist **/
    private static int foundArtist;


    /** the main method for the class **/
    public static String getLoc(String artistQuery) {
        ArtistQuery query = new ArtistQuery(artistQuery);
        String foundCountry;

        try {
            Thread.sleep(1250);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Was unable to pause.");
        }
        String url = "http://musicbrainz.org/ws/2/artist/?fmt=xml&query=artist:" + query.getEncoded();
        Document xmlDoc = loadDoc(url);


        if (xmlDoc == null) {
            System.err.println("Got invalid response for: " + query);
            return null;
        }

        if (!artistFound(query, xmlDoc)) {
            System.err.println("Was unable to find data for: " + query);
            return null;
        }

        foundCountry = findLocation(xmlDoc);
        if (foundCountry == null) {
            System.err.println("Was unable to find country data for: " + query);
            return null;
        }
        return foundCountry;
    }

    /** gets a Document file from a given url **/
    private static Document loadDoc(String url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder().parse(new URL(url).openStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("\nWas unable to load xml file from: " + url);
        }
    }

    /** checks if the given artist name matches the name of the first artist in the loaded document **/
    private static boolean artistFound(ArtistQuery query, Document xmlDoc) {
        foundArtist = -1;

        for (int i=0; i<ARTISTS_TO_CHECK; i++) {
            if (nameMatches(query, xmlDoc, i) || sortNameMatches(query, xmlDoc, i)) {
                foundArtist = i;
                return true;
            }
        }
        return false;
    }

    /** checks if the artist's name matches the sort name of the given artist **/
    private static boolean nameMatches(ArtistQuery query, Document xmlDoc, int artistIndex) {
        String xmlName = "";
        try {
            xmlDoc.getDocumentElement().normalize();

            NodeList allNodes = xmlDoc.getElementsByTagName("artist");
            Element firstElement = (Element) allNodes.item(artistIndex);
            xmlName = firstElement.getElementsByTagName("name").item(0).getTextContent();
            xmlName = ArtistQuery.unify(xmlName);

        } catch (Exception ignored) { }

        return ArtistQuery.encode(xmlName).equalsIgnoreCase(query.getEncoded()); // target has already been encoded
    }

    /** checks if the artist's name in the form 'surname, firstname' matches the sort name of the given artist **/
    private static boolean sortNameMatches(ArtistQuery query, Document xmlDoc, int artistIndex) {
        String xmlSortName = "";
        try {
            xmlDoc.getDocumentElement().normalize();

            NodeList allNodes = xmlDoc.getElementsByTagName("artist");
            Element firstElement = (Element) allNodes.item(artistIndex);
            xmlSortName = firstElement.getElementsByTagName("sort-name").item(0).getTextContent();
            xmlSortName = ArtistQuery.unify(xmlSortName);

        } catch (Exception ignored) { }

        return ArtistQuery.encode(xmlSortName).equalsIgnoreCase(query.getEncodedSortName()); // target has already been encoded
    }

    /** finds the location from the first artist entry in the given xml doc **/
    private static String findLocation(Document xmlDoc) {
        String currentAttempt = readCountry(xmlDoc);
        if (currentAttempt != null) {
            return currentAttempt;
        }

        currentAttempt = readArea(xmlDoc);
        if (currentAttempt != null) {
            return currentAttempt;
        }

        currentAttempt = readBeginArea(xmlDoc);
        return currentAttempt;
    }

    /** returns the data between the <country> tags, or null if they don't exist **/
    private static String readCountry(Document xmlDoc) {
        String country = null;
        try {
            xmlDoc.getDocumentElement().normalize();

            NodeList allNodes = xmlDoc.getElementsByTagName("artist");
            Element firstArtist = (Element) allNodes.item(foundArtist);
            country = firstArtist.getElementsByTagName("country").item(0).getTextContent();

        } catch (Exception ignored) { }

        return country;
    }

    /** returns the data between the <area>/<name> tags, or null if they don't exist **/
    private static String readArea(Document xmlDoc) {
        String area = null;
        try {
            xmlDoc.getDocumentElement().normalize();

            NodeList allNodes = xmlDoc.getElementsByTagName("artist");
            Element firstArtist = (Element) allNodes.item(foundArtist);
            Element areaElement = (Element) firstArtist.getElementsByTagName("area").item(0);
            area = areaElement.getElementsByTagName("name").item(0).getTextContent();

        } catch (Exception ignored) { }

        return area;
    }

    /** returns the data between the <begin-area>/<name> tags, or null if they don't exist **/
    private static String readBeginArea(Document xmlDoc) {
        String beginArea = null;
        try {
            xmlDoc.getDocumentElement().normalize();

            NodeList allNodes = xmlDoc.getElementsByTagName("artist");
            Element firstArtist = (Element) allNodes.item(foundArtist);
            Element areaElement = (Element) firstArtist.getElementsByTagName("begin-area").item(0);
            beginArea = areaElement.getElementsByTagName("name").item(0).getTextContent();

        } catch (Exception ignored) { }

        return beginArea;
    }

    /** prints an xml document **/
    private static void printXmlDocument(Document xmlDocument) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();

            // Uncomment if you do not require XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            //A character stream that collects its output in a string buffer,
            //which can then be used to construct a string.
            StringWriter writer = new StringWriter();

            //transform document to string
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));

            String xmlString = writer.getBuffer().toString();
            System.out.println(xmlString);                      //Print to console or logs
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
