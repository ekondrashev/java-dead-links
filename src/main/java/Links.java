import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public interface Links extends Iterable<URL> {

    class HTML implements Links {
        private String urlStr;
        private Map<URL, Integer> links;
        private List<String> links404 = new ArrayList<>();
        private List<String> links50X = new ArrayList<>();
        private Elements anchors;
        private List<URL> urls;
        private JSONObject result;
        private HTTP httpDefault;


        public HTML(String urlStr, HTTP httpDefault) {
            this.urlStr = urlStr;
            this.urls = new ArrayList<>();
            this.links = new HashMap<>();
            this.httpDefault = httpDefault;
            this.result = getResult(urlStr);
        }

        public JSONObject getResult(String urlStr) {
            links = getLinkMap();

            links.entrySet()
                    .forEach(entry -> {
                        if (entry.getValue() == 404) {
                            links404.add(entry.getKey().toString());
                        } else if (entry.getValue() >= 500 && entry.getValue() < 600) {
                            links50X.add(entry.getKey().toString());
                        }
                    });

            result = new JSONObject();
            result.put("url", urlStr);

            JSONObject jsonObject404 = new JSONObject();
            jsonObject404.put("size", links404.size());
            jsonObject404.put("urls", new JSONArray(links404));
            result.put("404", jsonObject404);

            JSONObject jsonObject50X = new JSONObject();
            jsonObject50X.put("size", links50X.size());
            jsonObject50X.put("urls", new JSONArray(links50X));
            result.put("50X", jsonObject50X);

            result.put("dead", links50X.size() + links404.size());
            result.put("total", urls.size());

            return this.result;
        }

        private Map<URL, Integer> getLinkMap() {
            Iterator<URL> iterator = iterator();
            Integer responseCode;
            while (iterator.hasNext()) {
                URL currURL = iterator.next();
                responseCode = getResponseCode(currURL);
                links.put(currURL, responseCode);
            }
            return links;
        }

        private Integer getResponseCode(URL href) {
            if (httpDefault == null) {
                System.out.println("httpDefault is null");
            }
            return httpDefault.response(href).code();
        }

        @Override
        public Iterator<URL> iterator() {
            try {
                Document document = Jsoup.connect(urlStr).get();
                anchors = document.select("a[href]");
                String attr;
                for (Element a : anchors) {
                    attr = a.attr("abs:href");
                    if (attr.equals("") || attr.contains("mailto") || attr.contains("tel") || attr.contains("javascript")) {
                        continue;
                    }
                    urls.add(new URL(attr));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return urls.iterator();
        }

        @Override
        public String toString() {
            return result.toString();
        }
    }
}
