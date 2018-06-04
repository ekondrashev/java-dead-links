import org.junit.*;

import java.io.*;

public class MainTestIt {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private String expectedOutput;
    private String url = "http://home.mcom.com/home/welcome.html";

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Before
    public void setExpectedOutput() throws IOException {
        expectedOutput = "";
        InputStream resourceAsStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("main_test_expected_output.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            expectedOutput += line;
        }
        expectedOutput = expectedOutput.replaceAll(" ", "");
        expectedOutput = expectedOutput.replaceAll("\n", "");
        expectedOutput = expectedOutput.replaceAll("\t", "");
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }


    @Test
    public void testDeadLinksMainReturnsJsonOutput() {
        Main.main(new String[] {url});
        Assert.assertEquals(expectedOutput, outContent.toString().trim());
    }
}
