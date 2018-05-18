package com.deadlinks;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MainTest {

    private final ByteArrayOutputStream outputContent = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outputContent));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(System.out);
    }

    @Test
    public void dead_links_test() throws JsonProcessingException {
        String[] url ={"https://junit.org/junit5/"};
        Main.main(url);
        assertEquals(getExpectedValue(), outputContent.toString());
    }

    private String getExpectedValue() {
        return "{" + System.lineSeparator() +
                "  \"url\" : \"https://junit.org/junit5/\"," + System.lineSeparator() +
                "  \"urlDetails\" : {" + System.lineSeparator() +
                "    \"0\" : {" + System.lineSeparator() +
                "      \"size\" : 5," + System.lineSeparator() +
                "      \"urls\" : [ \"/junit5/\", \"/junit5/docs/current/user-guide\", \"/junit5/docs/current/api\", \"/junit5/docs/current/user-guide\", \"/junit5/docs/current/api\" ]" + System.lineSeparator() +
                "    }" + System.lineSeparator() +
                "  }," + System.lineSeparator() +
                "  \"deadLinks\" : 5," + System.lineSeparator() +
                "  \"totalLinks\" : 12" + System.lineSeparator() +
                "}" + System.lineSeparator();
    }
}