package com;

import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDeadLinks {
       private String inputUrl;
  
    public FindDeadLinks(String targetUrl) {
        this.inputUrl = targetUrl;
    }

    public String sortLinks() {

        Map<URL, Integer> links = linksParser(inputUrl);
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


    public Map<URL, Integer> linksParser(String inputUrl) {
        try {
            Document document = Jsoup.connect(inputUrl).get();
            Elements elements = document.getElementsByTag("a");
            List<URL> urls = new ArrayList<>();
            URL url = null;
            try {
                url = new URL(inputUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e.getMessage());
            }
            String targetUrl = url.getProtocol() + url.getHost();
            elements.forEach(element -> {

                try {
                    String value = element.attr("href").trim();
                    if (value.startsWith("/")) {
                        value = targetUrl + value;
                    } else if (value.startsWith("#")) {
                        value = inputUrl + "/" + value;
                    }
                    urls.add(new URL(value));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            });

            Map<URL, Integer> allLinks = new HashMap<>();
            for (URL link : urls) {
                allLinks.put(link, getResponseCode(link));
            }
            return allLinks;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private int getResponseCode(URL link) {
        HttpURLConnection connection = null;
        int responseCode = 200;
        if (link.toString().length() == 0) {
            return 404;
        }
        try {
            if (link.getProtocol().equals("https")) {
                connection = (HttpsURLConnection) link.openConnection();
            } else if (link.getProtocol().equals("http")) {
                connection = (HttpURLConnection) link.openConnection();

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