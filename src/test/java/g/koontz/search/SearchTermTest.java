package g.koontz.search;

import org.junit.Test;

import static org.junit.Assert.*;

public class SearchTermTest {

    @Test
    public void hasTerm() {
        SearchTerm searchTerm = new SearchTerm("ABC");
        assertTrue(searchTerm.hasTerm("abcdef"));
        assertTrue(searchTerm.hasTerm("AbCdEf"));
        assertFalse(searchTerm.hasTerm("ZYXWVUT"));
    }

    @Test
    public void getTerm() {
        SearchTerm searchTerm = new SearchTerm("ABC");
        assertTrue(searchTerm.hasTerm("abc"));
        assertEquals("ABC", searchTerm.getTerm());
    }
}