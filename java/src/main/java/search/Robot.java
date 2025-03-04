package search;

import java.rmi.registry.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Robot {
    public static void main(String[] args) {
        try {
            Index index = (Index) LocateRegistry.getRegistry(8183).lookup("index");
            while (true) {
                String url = index.takeNext();
                System.out.println(url);
                Document doc = Jsoup.connect(url).get();
                System.out.println(doc);
                //Todo: Read JSOUP documentation and parse the html to index the keywords. 
                //Then send back to server via index.addToIndex(...)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
