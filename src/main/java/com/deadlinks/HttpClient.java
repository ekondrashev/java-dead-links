package com.deadlinks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient {

    private Map<Integer, List<String>> result;
    private List<String> successUrls;
    private List<String> failedUrls;
    private List<String> incorrectUrls;

    public HttpClient() {
        result = new HashMap<>();
        successUrls = new ArrayList<>();
        failedUrls = new ArrayList<>();
        incorrectUrls = new ArrayList<>();
    }

    public void checkUrls(String... urls) {
        List<String> links = getLinksFromHtml(urls);
        links.stream().forEach(url -> {
            if (verifyUrl(url)) {
                HttpURLConnection connection = null;
                try {
                    URL currentUrl = new URL(url);
                    connection = (HttpURLConnection) currentUrl.openConnection();
                    if (connection.getResponseCode() == UrlStatus.HTTP_OK.getStatusCode()) {
                        successUrls.add(url);
                        result.putIfAbsent(connection.getResponseCode(), successUrls);
                    } else {
                        failedUrls.add(url);
                        result.putIfAbsent(connection.getResponseCode(), failedUrls);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } else {
                incorrectUrls.add(url);
                result.putIfAbsent(0, incorrectUrls);
            }
        });
    }

    private List<String> getLinksFromHtml(String... urls) {
        List<String> linkList = new ArrayList<>();
        Arrays.stream(urls).forEach(url -> {
            try {
                Document page = Jsoup.connect(url).get();
                Elements links = page.select("a[href]");
                for (Element link: links) {
                    linkList.add(link.attr("href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return linkList;
    }

    private boolean verifyUrl(String url) {
        String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public Map<Integer, List<String>> getResult() {
        return result;
    }
}
