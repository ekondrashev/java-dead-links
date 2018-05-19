import java.io.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import static org.junit.Assert.assertEquals;

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
    String urlForChecking = "http://roofing.tilda.ws";
    String expectedResult = "{\"urlForCheck\":\"http://roofing.tilda.ws\",\"err404\":{\"size\":1,\"urls\":[\"http://roofing.tilda.ws/somepage.html\"]},\"err50x\":{\"size\":0,\"urls\":[]},\"dead\":1,\"total\":14}";
    DeadLinks deadLinks = new DeadLinks(urlForChecking);
    try {
      deadLinks.findDeadLinks();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    System.out.print(deadLinks);
    assertEquals(expectedResult.trim(), outContent.toString().trim());
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out);
    System.setErr(System.err);
  }

}
