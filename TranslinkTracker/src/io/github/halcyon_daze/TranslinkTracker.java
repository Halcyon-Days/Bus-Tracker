package io.github.halcyon_daze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TranslinkTracker {
    
    
    public static void main(String[] args) throws IOException {
 
        try {
            getRouteInfo(58090).printTimes();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    
    /*
     * @param stop number of the route stop to extract info from
     * 
     * @return a BusStop object on the routestop
     */
    public static BusStop getRouteInfo(int routeNo) throws IOException {
        String api = getAPIKey();
        return getRouteInfo("http://api.translink.ca/rttiapi/v1/stops/" + routeNo + "/estimates?apikey=" + api + "&timeframe=1440");
    }
    
    /*
     * @param url url of the route stop to extract info from
     * 
     * @return a BusStop object on the routestop
     */
    public static BusStop getRouteInfo(String url) throws IOException {

        Document doc = getPageAsDoc(url);
        NodeList nodes = doc.getElementsByTagName("Schedule");
        ArrayList<String> times = new ArrayList<String>();
        
        System.out.println("Bus times for route " + doc.getElementsByTagName("RouteName").item(0).getTextContent() + " in the " + doc.getElementsByTagName("Direction").item(0).getTextContent() + " direction acquired!");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element currentElement = (Element) nodes.item(i);
            
            times.add(currentElement.getElementsByTagName("ExpectedLeaveTime").item(0).getTextContent());
        }
        
        BusStop stopInfo = new BusStop( doc.getElementsByTagName("RouteNo").item(0).getTextContent(), 
                                        doc.getElementsByTagName("RouteName").item(0).getTextContent(), 
                                        doc.getElementsByTagName("Direction").item(0).getTextContent(),
                                        times );

        return stopInfo;
    }
    
    /*
     * @return API key located at apikey/keyfile.txt from top level directory
     */
    public static String getAPIKey() throws FileNotFoundException {
        String key = "";
        Scanner sc;
        
        try {
            sc = new Scanner(new File("apikey/keyfile.txt"));
            while(sc.hasNextLine()) {
                key = sc.nextLine();           
            }
            sc.close();
        } catch(FileNotFoundException e) {
            System.out.println("Invalid key file! \nKey file should be located at apikey/keyfile.txt, containing only the key in the file!\n");
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
    public static Document getPageAsDoc(String url) throws IOException{
        System.out.println("Connecting to " + url);
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc = null;
        
        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(url);
            
        } catch (Exception e) {
            System.out.println("Could not extract info from " + url);
            throw new java.io.IOException();
        }
        
        return doc;

    }
}
