package com.deadlinks;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public void shouldReturn404and50xLinksFromHTMLToJsonFormat() throws JsonProcessingException {
        String[] url ={"https://www.yegor256.com/elegant-objects.html", "--enable-http-recording=http_recording.json"};
        Main.main(url);
        assertEquals(returnExpectedValue(), outputContent.toString());
    }

    private String returnExpectedValue() {
        String expectedJson = "";
        try {
            expectedJson = Files.lines(
                    Paths.get(Objects.requireNonNull(
                            MainTest.class.getClassLoader().getResource("expected_json")).toURI()))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException | URISyntaxException | NullPointerException e) {
            e.printStackTrace();
        }
        return expectedJson + System.lineSeparator();
    }
}