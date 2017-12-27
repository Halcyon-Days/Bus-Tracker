package io.github.halcyon_daze;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TranslinkTracker {
    
    
    public static void main(String[] args) {
        String api;
        
        try {
            api = getAPIKey();
            
            for(String s: getTimes("http://api.translink.ca/rttiapi/v1/stops/58090/estimates?apikey=" + api + "&count=3&timeframe=1440")) {
                System.out.println(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    
    public static ArrayList<String> getTimes(String url) {
        Document doc = getPageAsDoc(url);
        NodeList nodes = doc.getElementsByTagName("Schedule");
        ArrayList<String> times = new ArrayList<String>();
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Element currentElement = (Element) nodes.item(i);
            
            times.add("Bus #" + i + " coming at " + currentElement.getElementsByTagName("ExpectedLeaveTime").item(0).getTextContent());
        }
        
        return times;
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
    public static Document getPageAsDoc(String url){
        System.out.println("Connecting to " + url);
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc = null;
        
        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(url);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not extract info from " + url);
        }
        
        return doc;

    }
}
