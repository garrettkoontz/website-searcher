package g.koontz;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MainTest {

    @org.junit.Test
    public void parseLineTest() {
        String line = "1,\"facebook.com/\",9616487,1688316928,9.54,9.34";
        assertEquals("facebook.com/",Main.parseLine(line));
    }

    @Test
    public void getUrlsTest() throws IOException {
        String fileName = "urls.txt";
        assertEquals(Arrays.asList("w3.org/", "apple.com/"), Main.getUrls(fileName));
    }
}