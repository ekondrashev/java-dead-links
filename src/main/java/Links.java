import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

interface Links extends Iterable<URL> {

  class HTML implements Links {

    private ArrayList<URL> urls;
    private String urlForCheck;
    private Err404 err404;
    private Err50x err50x;
    private int dead = 0;
    private int total = 0;

    public HTML(String urlForCheck, HTTP httpDefault) {
      urlForCheck = urlForCheck.trim();
      this.urlForCheck = urlForCheck.trim();
      this.err404 = new Err404();
      this.err50x = new Err50x();
      generateReport(httpDefault);
    }


    @Override
    public Iterator<URL> iterator() {
      this.urls = new ArrayList<>();
      try {
        parseHrefs();
      } catch (Exception e) {
        System.err.println("Illegal URL");
      }
      return this.urls.iterator();
    }

    private void parseHrefs() throws IOException {
      String protocol = urlForCheck.split("://")[0];
      Document document = Jsoup.connect(urlForCheck).get();
      Elements links = document.select("a[href]");

      for (Element link : links) {
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
          this.urls.add(new URL(href));
        }
      }
    }

    private void generateReport(HTTP http) {
      for (URL url : this) {
        int responseCode = http.code(url);
        if (responseCode == 404) {
          this.err404.urls.add(url.toString());
          this.err404.size++;
          this.dead++;
        }
        if (responseCode >= 500 && responseCode < 600) {
          this.err50x.urls.add(url.toString());
          this.err50x.size++;
          this.dead++;
        } else {
          this.total++;
        }
      }
    }

    @Override
    public String toString() {
      return getPrettyJson();
    }

    private String getPrettyJson() {
      Gson htmlGson = new GsonBuilder().setPrettyPrinting().create();
      Gson err404Gson = new GsonBuilder().setPrettyPrinting().create();
      Gson err50xGson = new GsonBuilder().setPrettyPrinting().create();
      JsonObject jsonObject = new JsonObject();

      jsonObject.addProperty("url", urlForCheck);
      jsonObject.add("404", err404Gson.toJsonTree(this.err404));
      jsonObject.add("50x", err50xGson.toJsonTree(this.err50x));
      jsonObject.addProperty("dead", this.dead);
      jsonObject.addProperty("total", this.total);
      return htmlGson.toJson(jsonObject);
    }

    private class Err404 {

      private int size = 0;
      private ArrayList<String> urls = new ArrayList<>();
    }

    private class Err50x {

      private int size = 0;
      private ArrayList<String> urls = new ArrayList<>();
    }
  }
}
