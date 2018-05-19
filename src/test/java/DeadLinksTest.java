import java.io.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import static org.junit.Assert.assertEquals;

/**
 * Java code implementing the below contract;
 Test calling main(String[] args) and verifying the program system output correctness.
 */

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
    String expectedResult = "{\"urlForCheck\":\"http://roofing.tilda.ws\",\"err404\":{\"size\":1,\"urls\":[\"http://roofing.tilda.ws/somepage.html\"]},\"err50x\":{\"size\":0,\"urls\":[]},\"dead\":1,\"total\":14}";
    FindDeadLinksApp.main(new String[]{"http://roofing.tilda.ws"});
    assertEquals(expectedResult.trim(), outContent.toString().trim());
  }

  @Test
  public void err() {
    String expectedError = "Illegal URL";
    FindDeadLinksApp.main(new String[]{"asdasd"});
    assertEquals(expectedError.trim(), errContent.toString().trim());
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out);
    System.setErr(System.err);
  }

}
