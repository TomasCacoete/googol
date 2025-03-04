package search;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;

public class IndexServer extends UnicastRemoteObject implements Index {
    private ArrayList<String> urlsToIndex;
    //private String[][] indexedItems;
    private HashMap<String, ArrayList<String>> indexedItems;

    public IndexServer() throws RemoteException {
        super();
        //This structure has a number of problems. The first is that it is fixed size. Can you enumerate the others?
        //urlsToIndex = new String[10];
        urlsToIndex = new ArrayList<String>();
        indexedItems = new HashMap<>();
    }

    public static void main(String args[]) {
        try {
            IndexServer server = new IndexServer();
            Registry registry = LocateRegistry.createRegistry(8181);
            registry.rebind("index", server);
            System.out.println("Server ready. Waiting for input...");

            //TODO: This approach needs to become interactive. Use a Scanner(System.in) to create a rudimentary user interface to:
            //1. Add urls for indexing
            //2. search indexed urls
            server.putNew("https://pt.wikipedia.org/wiki/Wikip%C3%A9dia:P%C3%A1gina_principal");

            // search words
            
            Scanner reader = new Scanner(System.in);
            while(true){
                System.out.println("Total :" + Runtime.getRuntime().totalMemory()/ (1024 * 1024));
                System.out.println("Free Mem:" + Runtime.getRuntime().freeMemory()/ (1024 * 1024));
                System.out.println("Looking for word...");
                System.out.println("Palavra encontrada " + server.searchWord(reader.nextLine()));
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private long counter = 0, timestamp = System.currentTimeMillis();;

    public String takeNext() throws RemoteException { // lê URL da fila de urls para robôs processarem
        if (urlsToIndex.size() == 0) return "";

        String url = urlsToIndex.get(0);
        urlsToIndex.remove(0);
        return url;
    }

    public void putNew(String url) throws java.rmi.RemoteException { // adiciona um URL à fila de urls para robôs verem
        urlsToIndex.add(url);
    }

    public void addToIndex(String word, String url) throws java.rmi.RemoteException {
        // word not found yet
        if (indexedItems.get(word) == null){
            indexedItems.put(word, new ArrayList<String>());
        }

        indexedItems.get(word).add(url); // adds url to word
    }

    
    public List<String> searchWord(String word) throws java.rmi.RemoteException {
        if (indexedItems.get(word) == null) return new ArrayList<String>();

        return indexedItems.get(word);
    }
}
