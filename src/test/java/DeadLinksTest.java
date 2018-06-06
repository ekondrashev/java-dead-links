import com.google.gson.*;
import java.io.*;
import java.net.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DeadLinksTest {

  private final ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
  private final ByteArrayOutputStream actualErrOutput = new ByteArrayOutputStream();
  private String expectedRegularOutput;
  private String expectedErrOutput;
  private int httpResponseCode;
  private String url = "http://roofing.tilda.ws";

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(actualOutput));
    System.setErr(new PrintStream(actualErrOutput));
  }

  @Before
  public void readExpectedRegularJSON() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonObject jsonObject = gson.fromJson(new InputStreamReader(
        getClass()
            .getClassLoader()
            .getResourceAsStream("expected_regular_output.json")), JsonObject.class);
    expectedRegularOutput = gson.toJson(jsonObject);
  }

  @Before
  public void readExpectedErrJSON() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonObject jsonObject = gson.fromJson(new InputStreamReader(
        getClass()
            .getClassLoader()
            .getResourceAsStream("expected_err_output.json")), JsonObject.class);
    expectedErrOutput = gson.toJson(jsonObject);
  }

  @Before
  public void checkConnection() {
    try {
      httpResponseCode = new HTTP.Default().code(new URL(url));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Main can return a report about checking html page for dead links
   * if there is a connection error, method returns an empty json
   * only with filled "url" field and fields with numerical values("size", "total", "dead")
   */
  @Test
  public void returnsReportAboutDeadLinks() {
    Main.main(new String[]{url});
    if (httpResponseCode == 200) {
      assertEquals(expectedRegularOutput.trim(), actualOutput.toString().trim());
    } else {
      assertEquals(expectedErrOutput.trim(), actualOutput.toString().trim());
    }
  }

  /**
   * Main can cause an error if the URL is incorrect
   */
  @Test
  public void returnsErrorIfIllegalURL() {
    String expectedErrorIllegalURL = "Illegal URL\n";
    Main.main(new String[]{"someIllegalURL"});
    if (httpResponseCode!=-1) {
      assertEquals(expectedErrorIllegalURL.trim(), actualErrOutput.toString().trim());
    }
  }

  /**
   * Main can cause an error if there is an connection error
   */
  @Test
  public void returnsErrorIfConnectionError() {
    String expectedConnectionError = "Connection error\n";
    Main.main(new String[]{url});
    if (httpResponseCode==-1) {
      assertEquals(expectedConnectionError.trim(), actualErrOutput.toString().trim());
    }
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out);
    System.setErr(System.err);
  }
}
