package g.koontz.search;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchCoordinator {

    private final SearchTerm searchTerm;
    private final Queue<String> urls;
    private final Map<String, Boolean> results;

    private final String header;
    private static final String OUTPUT_FORMAT = "%s,%s%n";
    private static final int MAX_THREADS = 20;
    private static final int MAX_TIMEOUT = 60000;

    public SearchCoordinator(SearchTerm searchTerm, List<String> urls) {
        this.searchTerm = searchTerm;
        this.header = String.format("url,contains %s%n", searchTerm.getTerm());
        this.urls = new ConcurrentLinkedQueue(urls);
        this.results = new ConcurrentHashMap<>();
    }

    private static URL makeUrl(String urlString) {
        try {
            return new URL("http://" + urlString);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    class SearcherThread implements Runnable {

        @Override
        public void run() {
            String urlString;
            Searcher searcher = new Searcher(searchTerm);
            while ((urlString = urls.poll()) != null) {
                URL url = makeUrl(urlString);
                results.put(urlString, url == null ? false : searcher.getResult(url));
            }
        }
    }


    public void runSearch() {
        Set<Thread> threads = new HashSet<>();
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread t = new Thread(new SearcherThread(),String.valueOf(i));
            threads.add(t);
            t.start();
        }
        for(Thread t : threads){
            try {
                t.join(MAX_TIMEOUT);
            } catch (InterruptedException e) {
                System.err.println("interrupted!");
                return;
            }
        }

    }

    private BufferedWriter getWriter(String fileName) throws IOException {
        //clobbers output file!
        Path path = new File(fileName).toPath();
        Files.deleteIfExists(path);
        return Files.newBufferedWriter(path);
    }

    public void writeOutput(String fileName) throws IOException {
        try (BufferedWriter output = getWriter(fileName)) {
            output.write(header);
            for (Map.Entry<String, Boolean> result : results.entrySet()) {
                output.write(String.format(OUTPUT_FORMAT, result.getKey(), result.getValue()));
            }
            output.flush();
        }
    }

}
