import java.io.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DeadLinksTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @Test
  public void out() {
    String expectedResult = "{\n"
        + "\t\"url\":\" http://roofing.tilda.ws\",\n"
        + "\t\"404\": {\n"
        + "\t\t\"size\": 1,\n"
        + "\t\t\"urls\": [http://roofing.tilda.ws/somepage.html]\t\n"
        + "},\n"
        + "\t\"50x\": {\n"
        + "\t\t\"size\": 0,\n"
        + "\t\t\"urls\": []\t\n"
        + "},\n"
        + "\t\"dead\": 1,\n"
        + "\t\"total\": 14\n"
        + "}";
    Main.main(new String[]{"http://roofing.tilda.ws"});
    assertEquals(expectedResult.trim(), outContent.toString().trim());
  }

  @Test
  public void err() {
    String expectedError = "Illegal URL\n";
    Main.main(new String[]{"asdasd"});
    assertEquals(expectedError.trim(), errContent.toString().trim());
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out);
    System.setErr(System.err);
  }

}
