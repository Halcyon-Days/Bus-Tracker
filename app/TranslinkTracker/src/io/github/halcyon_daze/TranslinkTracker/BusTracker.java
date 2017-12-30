package io.github.halcyon_daze.TranslinkTracker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
 * Code used to connect to translink website and retrieve bus times
 * Archived, switched to using translink api instead
 */

public class BusTracker {
    
    public static void main(String[] args) {
        //getTimes("https://nb.translink.ca/nextbus.ashx?cp=gssr%2FSeI8rdrYzRqLxH%2FfgZYTGg==;028");
        //getTimes("https://nb.translink.ca/text/stop/58088/route/028#");
        //System.out.println(getRequestURL("https://nb.translink.ca/text/stop/58088/route/028#"));
        getTimes(getRequestURL("https://nb.translink.ca/text/stop/58088/route/028#"));
        getTimes("https://nb.translink.ca/nextbus.ashx?cp=gssr%2F7zIWH3i1SeknjAV%2B%2Bc5w8Q==;028");
        
        //TO DO: extract link from nbt.initStopAndRoute('ibQxaITdKhy+SLw+h2e8aQ==', '028')//]]>, parse into ajax request and automate request process
        //Can extract, each link has specific cookies that need to be set

    }
    
    
    public static void getTimes(String url){
        System.out.println("Connecting to " + url);
        
        try {
            URL pageURL = new URL(url);
            
            HttpURLConnection urlConnection = (HttpURLConnection) pageURL.openConnection();
            urlConnection.setRequestProperty("Cookie", "ASP.NET_SessionId=k1qimeyofc43jpc1ibjilzlg;ARRAffinity=d930dc5fc3a61ca4548df4822e7cff4111b8d71cc60cb328700427e4bfd458f9;nb.cookie=enabled");
            urlConnection.setRequestMethod("GET");
            
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            
            System.out.print(result.toString());
                    
        } catch (IOException e) {
            System.out.println("Could not extract info from " + url);
            return; 
        }
    }
    
    public static void getTimes(ArrayList<String> url){
        System.out.println("Connecting to " + url.get(0));
        
        try {
            URL pageURL = new URL(url.get(0));
            
            HttpURLConnection urlConnection = (HttpURLConnection) pageURL.openConnection();
            System.out.println(url.get(1) + "nb.cookie=enabled");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Cookie", url.get(1) + "nb.cookie=enabled"); 
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestProperty("If-Modified-Since", "Sun, 31 Oct 2010 00:00:00 GMT");
            urlConnection.setRequestProperty("DNT", "1");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Cache-Control", "max-age=0");
            urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8"); 
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
            
            urlConnection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            
            System.out.print(result.toString());
                    
        } catch (IOException e) {
            System.out.println("Could not extract info from " + url.get(0));
            return; 
        }
    }
    
    private static String convertStreamToString (InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        
        String input;
        
        try {
            while ((input = reader.readLine()) != null) {
                sb.append(input).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
    }
    
    private static ArrayList<String> getRequestURL (String url) {
        System.out.println("Fetching " + url + "...");
        
        Document webPage;
        Connection connection;
        Map<String, String> cookies;
        ArrayList<String> returnURL = new ArrayList<String>();
        
        try {
        connection = Jsoup.connect(url);
        webPage = connection.get();
        cookies = connection.method(Method.POST).execute().cookies();
        
        Connection.Request cookiedata = connection.request();
        for(Connection.KeyVal key: cookiedata.data()) {
            System.out.println(key.key() + " : " + key.value());
        }
        
        } catch (IOException e) {
            System.out.println("Unable to connect to " + url);
            return returnURL;
        } 
        
        String cookieString = new String("");
        for (String key: cookies.keySet()) {
            cookieString += key + "=" + cookies.get(key) + " ; ";
            //System.out.println(key + " " + cookies.get(key));
        }
        
        Elements scripts = webPage.getElementsByTag("script");
        
        String request = "gssr/";
        ArrayList<String> parameters = extractParameters(scripts.last().html());

        for(String element : parameters) {
            System.out.println(element + "\n");
        }
        
        request += parameters.get(0) + ";" + parameters.get(1);

        returnURL.add("https://nb.translink.ca/nextbus.ashx?cp=" + request.replace("+", "%2B").replace("/", "%2F"));
        returnURL.add(cookieString);
        
        return returnURL;
        
    }
    
    private static ArrayList<String> extractParameters (String function) {
        /*
        if ( !function.matches(".*\\(.*\\).*") ) {
            throw new IllegalArgumentException("Not a valid function call");
        }*/
        
        ArrayList<String> functionParameters = new ArrayList<String>(Arrays.asList(function.replaceAll(".*\\[|.*\\(|\\).*", "")
                                                                                           .split(",")));
        
        for(int i = 0; i < functionParameters.size(); i++ ) {
            functionParameters.set(i, functionParameters.get(i).replaceAll("\"|'|\\s+",""));
        }
        
        return functionParameters;
    }
    /*
    private static void getCookies(String url) {
        System.out.println("Connecting to " + url);
        
        try {
            URL pageURL = new URL(url);
            
            HttpURLConnection urlConnection = (HttpURLConnection) pageURL.openConnection();
            Map<String, java.util.List<String>> cookies = urlConnection.getRequestProperties();
            
            for( String key : cookies.keySet()) {
                for (String item: cookies.get(key)) {
                    System.out.println(item);
                }
            }
            
        } catch (IOException e) {
            System.out.println("Could not extract info from " + url);
            return; 
        }
    }*/
}
