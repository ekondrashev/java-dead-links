import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private String expectedOutput = "";

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Before
    public void setExpectedOutputFromJsonFile() {
        try {
            InputStreamReader inputStream = new InputStreamReader(getClass()
                    .getClassLoader()
                    .getResourceAsStream("expected_output.json"));
            BufferedReader reader = new BufferedReader(inputStream);
            StringBuilder builder = new StringBuilder();

            for (String line; (line = reader.readLine()) != null ; ) {
                builder.append(line).append("\r\n");
            }

            expectedOutput = builder.toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void testEnteredURLIsIncorrect() {
        System.out.println(new Links.HTML("hts://www.google.com/").toString());
        assertEquals("You input incorrect URL\r\n", outContent.toString());
    }

    @Test
    public void testFoundDeadLinksAndPrintResultInPrettyJson() {
        System.out.println(new Links.HTML("https://mellivorasoft.com/").toString());

        assertEquals(expectedOutput, outContent.toString());
    }
}
