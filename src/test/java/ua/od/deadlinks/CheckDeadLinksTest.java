package ua.od.deadlinks;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CheckDeadLinksTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;


    private final String url = "https://od.isuo.org/ru/vnz/view/id/75905";
    private final String expected = "{\n" +
            "  \"url\": \"https://od.isuo.org/ru/vnz/view/id/75905\",\n" +
            "  \"404\": {\n" +
            "    \"size\": 2,\n" +
            "    \"urls\": [\n" +
            "      \"http://oadk.opu.ua\",\n" +
            "      \"oadt.at.ua\"\n" +
            "    ]\n" +
            "  },\n" +
            "  \"50x\": {\n" +
            "    \"size\": 0,\n" +
            "    \"urls\": []\n" +
            "  },\n" +
            "  \"dead\": 2,\n" +
            "  \"total\": 132\n" +
            "}\n";


    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }


    @Test
    public void checkBrokenLinks(){
        DeadLinksChecker.main(new String[]{url});
        assertEquals(expected, outContent.toString());
    }


    @DisplayName("Negative test")
    @Test
    public void checkBrokenLinksNegative(){
        DeadLinksChecker.main(new String[]{url});
        assertNotEquals(expected+1, outContent.toString());
    }

}
