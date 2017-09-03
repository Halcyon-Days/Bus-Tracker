package io.github.halcyon_daze;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BusTracker {
    
    public static void main(String[] args) {
        getTimes("https://nb.translink.ca/nextbus.ashx?cp=gssr%2FSeI8rdrYzRqLxH%2FfgZYTGg==;028");
        //getTimes("https://nb.translink.ca/text/stop/58088/route/028#");
        System.out.println(getRequestURL("https://nb.translink.ca/text/stop/58088/route/028#"));
        getTimes(getRequestURL("https://nb.translink.ca/text/stop/58088/route/028#"));
        //getTimes("https://nb.translink.ca/nextbus.ashx?cp=gssr%2Fr1IRtM55P6ChF3OTKucevg==;028");
        //TO DO: extract link from nbt.initStopAndRoute('ibQxaITdKhy+SLw+h2e8aQ==', '028')//]]>, parse into ajax request and automate request process
        //Can extract, each link has specific cookies that need to be set

    }
    
    
    public static void getTimes(String url){
        System.out.println("Connecting to " + url);
        
        try {
            URL pageURL = new URL(url);
            
            HttpURLConnection urlConnection = (HttpURLConnection) pageURL.openConnection();
            urlConnection.setRequestProperty("Cookie", "_utma=61047657.704264432.1503808521.1503808521.1503812936.2;_utmz=61047657.1503808521.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none);ASP.NET_Sessionid=xst54zciexwzgrgr4eg5pzxa;ARRAffinity=d930dc5fc3a61ca4548df4822e7cff4111b8d71cc60cb328700427e4bfd458f9;nb.cookie=enabled;_utmb=61047657.1.10.1503812936;_utmc=61047657;_utmt=1;_ga=GA1.2.704264432.1503808521;_gid=GA1.2.1990847912.1503812943;nb.sgn=SnVuVGFuZw0K");
            urlConnection.setRequestMethod("GET");
            
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            
            System.out.print(result.toString());
                    
        } catch (IOException e) {
            System.out.println("Could not extract info from " + url);
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
    
    private static String getRequestURL (String url) {
        System.out.println("Fetching " + url + "...");
        
        Document webPage;
        try {
        webPage = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Unable to connect to " + url);
            return "";
        } 
        
        Elements scripts = webPage.getElementsByTag("script");
        
        String request = "gssr/";
        ArrayList<String> parameters = extractParameters(scripts.last().html());

        for(String element : parameters) {
            System.out.println(element + "\n");
        }
        
        request += parameters.get(0) + ";" + parameters.get(1);
        
        System.out.println(request);
        return "https://nb.translink.ca/nextbus.ashx?cp=" + request.replace("/\\+/g", "%2B").replace("/", "%2F");
        
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
}
