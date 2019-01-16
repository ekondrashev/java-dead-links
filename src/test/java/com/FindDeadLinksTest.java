package com;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FindDeadLinksTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    private final String url = "http://kosumi.i-go.in.ua/links.php?lng=1";
    private final String expected = "{\n" +
            "  \"url\": \"http://kosumi.i-go.in.ua/links.php?lng\\u003d1\",\n" +
            "  \"404\": {\n" +
            "    \"size\": 1,\n" +
            "    \"links\": [\n" +
            "      \"https://vk.com/kosumiclubode\"\n" +
            "    ]\n" +
            "  },\n" +
            "  \"50x\": {\n" +
            "    \"size\": 0,\n" +
            "    \"links\": []\n" +
            "  },\n" +
            "  \"dead\": 1,\n" +
            "  \"total\": 8\n" +
            "}\n";


    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void findLinks()  {
        Main.main(new String[]{url});
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void findLinksNegative()  {
        Main.main(new String[]{url});
        assertNotEquals(expected+1, outContent.toString());
    }
}
