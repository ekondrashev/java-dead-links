package com;

import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDeadLinks {
    public static void main(String[] args) {
        String url = "https://odessa.sabinka.info/";
        System.out.println(new FindDeadLinks(url).sortLinks());
    }


    private String inputUrl;

    public FindDeadLinks(String targetUrl) {
        this.inputUrl = targetUrl;
    }

    public String sortLinks() {
        Map<String, Integer> links = linksParser(inputUrl);
        LinksByResponse links404 = new LinksByResponse();
        LinksByResponse links50X = new LinksByResponse();

        links.entrySet()
                .forEach(entry -> {
                    if (entry.getValue() == 404) {
                        links404.addLink(entry.getKey());
                    } else if (entry.getValue() >= 500 && entry.getValue() < 510) {
                        links50X.addLink(entry.getKey());
                    }
                });

        Results results = new Results();
        results.setUrl(inputUrl);
        results.setNotFound(links404);
        results.setServerError(links50X);
        results.setDeadLinksQuantity(links50X.getSize() + links404.getSize());
        results.setTotalLinks(links.size());

        return new GsonBuilder().setPrettyPrinting().create().toJson(results);
    }

    public Map<String, Integer> linksParser(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByTag("a");
            List<String> urls = new ArrayList<>();
            elements.forEach(element -> urls.add(element.attr("href")));

            Map<String, Integer> allLinks = new HashMap<>();
            for (String link : urls) {
                allLinks.put(link.trim(), getResponseCode(link.trim()));
            }
            return allLinks;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getResponseCode(String link) {
        URL url;
        HttpURLConnection connection = null;
        int responseCode = 200;
        if (link.length() == 0) {
            return 404;
        }
        if (link.startsWith("/")) {
            link = inputUrl + link;
        } else if (link.startsWith("#")) {
            link = inputUrl + "/" + link;
        }
        try {
            url = new URL(link);
            if (link.startsWith("https")) {
                connection = (HttpsURLConnection) url.openConnection();
            } else if (link.startsWith("http")) {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setConnectTimeout(5000);
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            responseCode = 404;
        } finally {
            if (connection != null) connection.disconnect();
        }
        return responseCode;
    }
}