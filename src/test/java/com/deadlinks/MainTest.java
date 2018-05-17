package com.deadlinks;

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
    public void dead_links_test() {
        String[] url ={"https://junit.org/junit5/"};
        Main.main(url);
        assertEquals(getExpectedValue(), outputContent.toString());
    }

    private String getExpectedValue() {
        return "{0=[/junit5/, /junit5/docs/current/user-guide, /junit5/docs/current/api, /junit5/docs/current/user-guide, /junit5/docs/current/api], 200=[https://github.com/junit-team/junit5/, https://github.com/junit-team/junit5/, https://github.com/junit-team/junit5/wiki/Adding-your-talk-to-the-JUnit-website], 301=[http://junit.org/junit4/, http://stackoverflow.com/questions/tagged/junit5, http://junit.org/junit4/junit-lambda.html, http://junit.org/junit4/junit-lambda-campaign.html]}"
                + System.lineSeparator();
    }
}