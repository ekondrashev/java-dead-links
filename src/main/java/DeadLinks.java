import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class DeadLinks implements Serializable {

  private static final long serialVersionUID = 1964397694782582852L;
  private String urlForCheck;
  private final Err404 err404 = new Err404();
  private final Err50x err50x = new Err50x();
  private int dead = 0;
  private int total = 0;

  public DeadLinks(String urlForCheck) {
    urlForCheck = urlForCheck.trim();
    this.urlForCheck = urlForCheck.trim();
  }


  public void findDeadLinks() throws IOException {
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
        try {
          connection.setRequestMethod("GET");
          connection.connect();
        } catch (IOException e) {
          System.err.println(url + " : " + e.getMessage());
        }

        int code = -1;
        try {
          code = connection.getResponseCode();
          if (code == 404) {
            this.err404.urls.add(href);
            this.err404.size++;
            this.dead++;
          }
          if (code >= 500 && code < 600) {
            this.err50x.urls.add(href);
            this.err50x.size++;
            this.dead++;
          } else {
            this.total++;
          }
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
      }
    }
  }

  @Override
  public String toString() {
    return new com.google.gson.Gson().toJson(this);
  }

  private class Err404 {

    private int size = 0;
    private ArrayList<String> urls = new ArrayList<>();

//    @Override
//    public String toString() {
//      Object[] array = urls.toArray();
//      return "404{" +
//          "size:" + array.length +
//          "urls:" + Arrays.toString(array) +
//          '}';
//    }
  }

  private class Err50x {

    private int size = 0;
    private ArrayList<String> urls = new ArrayList<>();

//    @Override
//    public String toString() {
//      Object[] array = urls.toArray();
//      return "50x{" +
//          "size:" + array.length +
//          "urls:" + Arrays.toString(array) +
//          '}';
//    }
  }
}






/*
    return "{\n"
        + "    \"url\": \"" + this.urlForCheck + "\""
        + "    \"404\":  {\n"
        + "        \"size\":" + this.err404.size() + ","
        + "\t\"urls\":" + this.err404.toString()
        + "    },\n"
        + "    \"50x\": {\n"
        + "        \"size\":" + this.err50x.size() + ","
        + "\t\"urls\":" + this.err50x.toString()
        + "    }\n"
        + "    \"dead\":" + this.dead+","
        + "    \"total\":" + this.total
        + "}";
 */