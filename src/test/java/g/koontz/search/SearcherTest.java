package g.koontz.search;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SearcherTest {

    @Test
    public void getResultTrue() throws MalformedURLException {
        Searcher searcher = new Searcher(new SearchTerm("ABC"));
        assertTrue(searcher.getResult(new URL("http://abc.com")));
    }

    @Test
    public void getResultTrue2() throws MalformedURLException {
        Searcher searcher = new Searcher(new SearchTerm("PrIvAcY PoLiCy"));
        assertTrue(searcher.getResult(new URL("http://abc.com")));
    }


    @Test
    public void getResultFalse() throws MalformedURLException {
        Searcher searcher = new Searcher(new SearchTerm("I really hope this string never actually appears on this page."));
        assertFalse(searcher.getResult(new URL("http://www.abc.com")));
    }

    @Test
    public void getResultError() throws MalformedURLException {
        Searcher searcher = new Searcher(new SearchTerm("twitter"));
        searcher.getResult(new URL("http://t.co"));
    }
}