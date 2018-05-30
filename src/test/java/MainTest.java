import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

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
    public void testEnteredURLIsIncorrect() {
        System.out.print(new Links.HTML("hts://www.google.com/").toString());
        assertEquals("You input incorrect URL", outContent.toString());
    }

    @Test
    public void testFoundDeadLinksAndPrintResultInPrettyJson() {
        System.out.print(new Links.HTML("https://mellivorasoft.com/").toString());

        String expectedResult = "{\r\n" +
                "  \"url\" : \"https://mellivorasoft.com/\",\r\n" +
                "  \"404\" : {\r\n" +
                "    \"size\" : 1,\r\n" +
                "    \"urls\" : [ \"https://mellivorasoft.com/case-studies\" ]\r\n" +
                "  },\r\n" +
                "  \"dead\" : 1,\r\n" +
                "  \"total\" : 25\r\n" +
                "}";

        assertEquals(expectedResult, outContent.toString());
    }
}