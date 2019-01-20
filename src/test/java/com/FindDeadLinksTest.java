package com;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FindDeadLinksTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private final String url = "http://kosumi.i-go.in.ua/links.php?lng=1";
    private String expected = "";


    @Before
    public void setExpectedOutputFromJsonFile() {
        try {
            InputStreamReader inputStream = new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("expected_result.json"));
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();

            for (String line; (line = bufferedReader.readLine()) != null ; ) {
                stringBuilder.append(line).append("\n");
            }
            expected = stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

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
