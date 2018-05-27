package io.github.vladzasoba;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainTestIt {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private String expectedOutput = "{\"total\":15,\"404\":{\"urls\":[],\"size\":0},\"dead\":0,\"50X\":{\"urls\":[],\"size\":0},\"url\":\"http://home.mcom.com/home/welcome.html\"}\r\n";
    private String url = "http://home.mcom.com/home/welcome.html";

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
    public void testDeadLinksMain_returnsJsonOutput() {
        Main.main(new String[] {url});
        Assert.assertEquals(expectedOutput, outContent.toString());
    }
}
