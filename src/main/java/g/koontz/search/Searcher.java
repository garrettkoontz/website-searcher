package g.koontz.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class Searcher {

    private final SearchTerm searchTerm;
    private static final int TIMEOUT = 30000;//httpie default timeout seems good enough.
    private static final int MAX_DEPTH = 8;//won't follow infinite redirects.

    public Searcher(SearchTerm searchTerm) {
        this.searchTerm = searchTerm;
    }

    private static HttpURLConnection resolveConnection(URL url, int depth) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(TIMEOUT);
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        connection.setInstanceFollowRedirects(true);
        HttpURLConnection.setFollowRedirects(true);
        boolean redirect = false;
        int status;
        try {
            status = connection.getResponseCode();
        } catch (UnknownHostException e) {
            String host = url.getHost();
            if (!host.contains("www")) {
                return resolveConnection(new URL("http://www." + url.getHost()), depth);
            } else {
                throw e;
            }
        }
        //used from mkyong
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER)
                redirect = true;
        }
        if (redirect) {
            if (depth < MAX_DEPTH) {
                String newUrl = connection.getHeaderField("Location");
                if (newUrl != null && newUrl.contains("http")) {
                    return resolveConnection(new URL(newUrl), depth++);
                } else {
                    return resolveConnection(new URL(url, newUrl), depth++);
                }
            }
        }
        return connection;
    }

    public boolean getResult(URL url) {
        try {
            HttpURLConnection connection = resolveConnection(url, 0);
            connection.getResponseCode();
            try (InputStream stream = connection.getErrorStream() == null ? connection.getInputStream() : connection.getErrorStream();
                 BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
                String input;
                while ((input = in.readLine()) != null) {
                    if (searchTerm.hasTerm(input)) return true;
                }
                return false;
            }
        } catch (IOException e) {
            System.err.format("unable to read url: %s;%n", url);
            return false;
        }
    }

}
