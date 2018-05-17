package com.deadlinks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MainTest {

    private final ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
    private HttpClient client;

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
        client = new HttpClient();
        client.checkUrls();
    }
}