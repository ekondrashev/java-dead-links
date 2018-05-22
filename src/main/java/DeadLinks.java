import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class DeadLinks implements Serializable {

  private static final long serialVersionUID = 1964397694782582852L;
  private final Err404 err404 = new Err404();
  private final Err50x err50x = new Err50x();
  private String urlForCheck;
  private int dead = 0;
  private int total = 0;

  public DeadLinks(String urlForCheck) {
    urlForCheck = urlForCheck.trim();
    this.urlForCheck = urlForCheck.trim();
  }


  public void findDeadLinks() throws IOException, IllegalArgumentException {
    String protocol = urlForCheck.split("://")[0];
    Document document = Jsoup.connect(urlForCheck).get();
    Elements links = document.select("a[href]");

    for (Element link : links) {
      //or try HttpClient
      String href = link.attr("href");
      if (!(href.contains("tel:")) && !(href.contains("javascript:"))) {
        if (href.matches("^//.*?")) {
          href = protocol + ":" + href;
        } else if (!href.matches("^http.*?")) {
          if (!href.matches("^/.*?")) {
            href = urlForCheck + "/" + href;
          } else {
            href = urlForCheck + href;
          }
        }
        URL url = new URL(href);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.connect();

        int code = -1;
        code = connection.getResponseCode();
        if (code == 404) {
          this.err404.urls.add(href);
          this.dead++;
        }
        if (code >= 500 && code < 600) {
          this.err50x.urls.add(href);
          this.dead++;
        } else {
          this.total++;
        }
      }
    }
  }

  @Override
  public String toString() {
    return "{\n" +
        "\t\"url\":\" " + urlForCheck + "\",\n" +
        "\t\"404\": {\n" +
        "\t\t\"size\": " + err404.urls.size() + ",\n" +
        "\t\t\"urls\": " + err404.urls +
        "\t\n},\n" +
        "\t\"50x\": {\n" +
        "\t\t\"size\": " + err50x.urls.size() + ",\n" +
        "\t\t\"urls\": " + err50x.urls +
        "\t\n},\n" +
        "\t\"dead\": " + dead + ",\n" +
        "\t\"total\": " + total +
        "\n}";
  }

  private class Err404 {

    private ArrayList<String> urls = new ArrayList<>();
  }

  private class Err50x {

    private ArrayList<String> urls = new ArrayList<>();
  }
}