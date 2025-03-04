package search;

import java.rmi.registry.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Robot {
    public static void main(String[] args) {
        try {
            Index index = (Index) LocateRegistry.getRegistry(8181).lookup("index");
            while (true) {
                String url = index.takeNext();
                System.out.println("\nEsste é p url:" + url);
                if (url == "") continue;
                Document doc = Jsoup.connect(url).get(); // all HTML
                
                // put links in queue
                Elements links = doc.select("a[href]"); // ignores the a without href
                for (Element e: links){
                    index.putNew(e.attr("abs:href")); // adds links to list
                }

                // 'indexate' words to urls
                String text = doc.body().text();
                String[] words = text.split("\\W+"); // illegal chars
                for (String s: words){
                    index.addToIndex(s, url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
