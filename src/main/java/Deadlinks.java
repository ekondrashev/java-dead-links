import java.io.*;
import java.net.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Deadlinks {

  private String urlForCheck;
  private String[] err404;
  private String[] err50x;
  private int total = 0;
  private int dead = 0;

  public Deadlinks(String urlForCheck) {
    this.urlForCheck = urlForCheck;
  }

  public void findDeadLinks() throws IOException {
    String protocol = urlForCheck.split("://")[0];
    Document document = Jsoup.connect(urlForCheck).get();
    Elements links = document.select("a[href]");

    for (Element link : links) {
      //or try HttpClient
      String href = link.attr("href");
      if (href.matches("^//.*?")) {
        href = protocol + ":" + href;
      } else if (!href.matches("^http.*?")) {
        href = urlForCheck+ "/" + href;
      }
      URL url = new URL(href);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("GET");

      try {
        connection.connect();
      } catch (IOException e) {
        System.out.println(url + " : " + e.getMessage());
      }

      int code = 0;
      try {
        code = connection.getResponseCode();
      } catch (IOException e) {
        e.getMessage();
      }
      System.out.println(url + " : " + code);

    }

  }

  public String getUrlForCheck() {
    return urlForCheck;
  }

  public void setUrlForCheck(String urlForCheck) {
    this.urlForCheck = urlForCheck;
  }

  public String[] getErr404() {
    return err404;
  }

  public void setErr404(String[] err404) {
    this.err404 = err404;
  }

  public String[] getErr50x() {
    return err50x;
  }

  public void setErr50x(String[] err50x) {
    this.err50x = err50x;
  }

  @Override
  public String toString() {
    return "Deadlinks{" +
        "urlForCheck='" + urlForCheck + '\'' +
        ", err404:" + err404.length +
        ", err50x:" + err50x.length +
        '}';
  }
}
