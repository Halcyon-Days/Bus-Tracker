package io.github.halcyon_daze;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class BusTracker {
    
    public static void main(String[] args) {
        getTimes("https://nb.translink.ca/text/stop/58088/route/028#");
        //getTimes("https://ca.yahoo.com/");

    }
    
    
    public static void getTimes(String url){
        System.out.println("Connecting to " + url);
        
        try {
            WebClient webClient = new WebClient(BrowserVersion.EDGE);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(true);

            webClient.waitForBackgroundJavaScript(100000);
            
            HtmlPage page = webClient.getPage(url);
            webClient.close();
            
            Document docPage = Jsoup.parse(page.asXml());
            
            Elements schedule = docPage.select("div.schedule");
            
            System.out.println("Printing out schedule: \n" + docPage.toString());
            
        } catch (IOException e) {
            System.out.println("Could not extract info from " + url);
            return; 
        }
    }
}
