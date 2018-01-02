package io.github.halcyon_daze.TranslinkTracker;

import android.content.Context;
import android.content.res.AssetManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TranslinkTracker {


    /*
     * @param stop number of the route stop to extract info from
     * 
     * @return a BusStop object on the routestop
     */
    public static BusStop getRouteInfo(Context context, String routeNo) throws FileNotFoundException, IOException  {
        String api = getAPIKey(context);
        return getRouteInfo("http://api.translink.ca/rttiapi/v1/stops/" + routeNo + "/estimates?apikey=" + api + "&timeframe=1440&count=6", routeNo);
    }
    
    /*
     * @param url url of the route stop to extract info from
     * 
     * @return a BusStop object on the routestop
     */
    public static BusStop getRouteInfo(String url, String routeNo) throws IOException {

        Document doc = getPageAsDoc(url);
        NodeList nodes = doc.getElementsByTagName("Schedule");
        ArrayList<String> times = new ArrayList<String>();
        
        System.out.println("Bus times for route " + doc.getElementsByTagName("RouteName").item(0).getTextContent() + " in the " + doc.getElementsByTagName("Direction").item(0).getTextContent() + " direction acquired!");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element currentElement = (Element) nodes.item(i);
            
            times.add(currentElement.getElementsByTagName("ExpectedLeaveTime").item(0).getTextContent());
        }
        
        BusStop stopInfo = new BusStop( String.valueOf(routeNo),
                                        doc.getElementsByTagName("RouteNo").item(0).getTextContent(),
                                        doc.getElementsByTagName("RouteName").item(0).getTextContent(), 
                                        doc.getElementsByTagName("Direction").item(0).getTextContent(),
                                        times );

        return stopInfo;
    }
    
    /*
     * @return API key located at apikey/keyfile.txt from top level directory
     */
    public static String getAPIKey(Context context) throws FileNotFoundException {
        String key = "";
        Scanner sc;
        AssetManager assetManager = context.getAssets();

        try {
            sc = new Scanner(assetManager.open("keyfile.txt"));
            while(sc.hasNextLine()) {
                key = sc.nextLine();           
            }
            sc.close();
        } catch(Exception e) {
            System.out.println("Invalid key file! \nKey file should be located at keyfile.txt, containing only the key in the file!\n");
            throw new FileNotFoundException();
        }
        System.out.println("Acquired key = " + key + "at apikey/keyfile.txt");
        return key;
    }
    
    /*
     * @param url
     * @return XML Document of the contents of what is at the url, may return null if invalid url
     * With reference to https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
     */
    public static Document getPageAsDoc(String url) throws IOException, UnsupportedOperationException{
        System.out.println("Connecting to " + url);
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc = null;
        
        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new URL(url).openStream()));

        } catch (IOException e) {
            System.out.println("Could not extract info from " + url);
            throw new java.io.IOException();
        } catch (Exception e) {
            System.out.println("Parse error");
            throw new UnsupportedOperationException();
        }

        return doc;

    }
}
