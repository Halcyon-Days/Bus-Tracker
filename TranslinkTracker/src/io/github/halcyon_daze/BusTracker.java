package io.github.halcyon_daze;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BusTracker {
    
    public static void main(String[] args) {
        getTimes("https://nb.translink.ca/nextbus.ashx?cp=gssr%2FSeI8rdrYzRqLxH%2FfgZYTGg==;028");
        //getTimes("https://ca.yahoo.com/");

    }
    
    
    public static void getTimes(String url){
        System.out.println("Connecting to " + url);
        
        try {
            
            URL pageURL = new URL(url);
            
            HttpURLConnection urlConnection = (HttpURLConnection) pageURL.openConnection();
            urlConnection.setRequestProperty("Cookie", "_utsma=61047657.704264432.1503808521.1503808521.1503812936.2;_utmz=61047657.1503808521.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none);ASP.NET_Sessionid=xst54zciexwzgrgr4eg5pzxa;ARRAffinity=d930dc5fc3a61ca4548df4822e7cff4111b8d71cc60cb328700427e4bfd458f9;nb.cookie=enabled;_utmb=61047657.1.10.1503812936;_utmc=61047657;_utmt=1;_ga=GA1.2.704264432.1503808521;_gid=GA1.2.1990847912.1503812943");
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
}
