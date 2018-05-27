package io.github.vladzasoba;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Links {

    static class HTML extends Links {
        private Map<String, Integer> links;
        private List<String> links404;
        private List<String> links50X;
        private JSONObject outputJson = new JSONObject();

        public HTML(String url) {
            try {
                outputJson = parseUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public JSONObject parseUrl(String url) throws IOException {
            links = getLinkMap(url);
            links404 = new ArrayList<>();
            links50X = new ArrayList<>();

            links.entrySet()
                    .forEach(entry -> {
                        if (entry.getValue() == 404) {
                            links404.add(entry.getKey());
                        } else if (entry.getValue() >= 500 && entry.getValue() < 600) {
                            links50X.add(entry.getKey());
                        }
                    });

            JSONObject outputJson = new JSONObject();
            outputJson.put("url", url);

            JSONObject jsonObject404 = new JSONObject();
            jsonObject404.put("size", links404.size());
            jsonObject404.put("urls", new JSONArray(links404));
            outputJson.put("404", jsonObject404);

            JSONObject jsonObject50X = new JSONObject();
            jsonObject50X.put("size", links50X.size());
            jsonObject50X.put("urls", new JSONArray(links50X));
            outputJson.put("50X", jsonObject50X);

            outputJson.put("dead", links50X.size() + links404.size());
            outputJson.put("total", links.entrySet().size());

            return outputJson;
        }

        private Map<String, Integer> getLinkMap(String url) throws IOException {
            Document document = Jsoup.connect(url).get();
            Elements anchors = document.select("a[href]");

            Map<String, Integer> linkMap = new HashMap<>();
            HttpURLConnection connection;
            String href;
            Integer responceCode;

            for (Element a : anchors) {
                href = a.attr("abs:href");
                if (href.equals("") || href.contains("mailto") || href.contains("tel") || href.contains("javascript")) {
                    continue;
                }
                responceCode = getResponseCode(href);
                linkMap.put(href, responceCode);
            }

            return linkMap;
        }

        private Integer getResponseCode(String href) {
            HttpURLConnection connection;
            Integer responseCode = 0;

            try {
                connection = (HttpURLConnection) new URL(href).openConnection();
                responseCode = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseCode;
        }

        @Override
        public String toString() {
            return outputJson.toString();
        }
    }
}
