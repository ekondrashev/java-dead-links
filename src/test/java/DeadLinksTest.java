import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class DeadLinksTest {

  private final ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
  private final ByteArrayOutputStream actualErrOutput = new ByteArrayOutputStream();
  private String expectedOutput;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(actualOutput));
    System.setErr(new PrintStream(actualErrOutput));
  }

  @Before
  public void readExpectedJSON() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonObject jsonObject = gson.fromJson(new InputStreamReader(
        getClass()
            .getClassLoader()
            .getResourceAsStream("expected_output.json")), JsonObject.class);

    expectedOutput = gson.toJson(jsonObject);
  }

  /**
   * Main can return a report about checking html page for dead links
   */
  @Test
  public void returnsReportAboutDeadLinks() {
    Main.main(new String[]{"http://roofing.tilda.ws"});
    assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
  }

  /**
   * Main can cause an error if the URL is incorrect
   */
  @Test
  public void returnsErrorInChecking() {
    String expectedError = "Illegal URL\n";
    Main.main(new String[]{"asdasd"});
    assertEquals(expectedError.trim(), actualErrOutput.toString().trim());
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out);
    System.setErr(System.err);
  }

}
