package g.koontz;

import g.koontz.search.SearchCoordinator;
import g.koontz.search.SearchTerm;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String INPUT_FILE_NAME = "urls.txt";
    private static final String OUTPUT_FILE_NAME = "results.txt";

    public static void main(String[] args) {
        String findMe = "jobs";
        String urlsPath = INPUT_FILE_NAME;
        if (args.length >= 1) {
            urlsPath = args[0];
        }
        SearchTerm searchTerm = new SearchTerm(findMe);
        try {
            SearchCoordinator searchCoordinator =
                    new SearchCoordinator(searchTerm, getUrls(urlsPath));
            searchCoordinator.runSearch();
            searchCoordinator.writeOutput(OUTPUT_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Can't find urls file.");
            System.exit(1);
        }

    }

    static List<String> getUrls(String inputFileName) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(inputFileName);
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(is))) {
            return reader.lines().map(x -> parseLine(x)).filter(x -> !x.equals("URL"))
                    .collect(Collectors.toList());
        }
    }


    static String parseLine(String line) {
        return line.split(",")[1].replace("\"", "");
    }
}
