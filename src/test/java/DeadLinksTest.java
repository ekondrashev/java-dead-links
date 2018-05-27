import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class DeadLinksTest {
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
        HTTPClient httpClient = new HTTPClient();
        assertEquals(httpClient.foundDeadLinks("hts://www.google.com/"),"You input incorrect URL");
    }

    @Test
    public void testFoundDeadLinksAndPrintResultInPrettyJson() {
        HTTPClient httpClient = new HTTPClient();
        System.out.println(httpClient.foundDeadLinks("https://mellivorasoft.com/"));

        String expectedResult = "{\r\n" +
                "  \"url\" : \"https://mellivorasoft.com/\",\r\n" +
                "  \"404\" : {\r\n" +
                "    \"size\" : 1,\r\n" +
                "    \"urls\" : [ \"https://mellivorasoft.com/case-studies\" ]\r\n" +
                "  },\r\n" +
                "  \"dead\" : 1,\r\n" +
                "  \"total\" : 25\r\n" +
                "}\r\n";

        assertEquals(expectedResult, outContent.toString());
    }
}
