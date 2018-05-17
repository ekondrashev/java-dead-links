package com.deadlinks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient {

    private Map<Integer, String> result;

    public void checkUrls(String... urls) {
        List<String> links = getLinksFromHtml(urls);
        links.forEach(url -> {
            if (verifyUrl(url)) {
                try {
                    URL currentUrl = new URL(url);
                    HttpsURLConnection connection = (HttpsURLConnection) currentUrl.openConnection();
                    if (connection.getResponseCode() == UrlStatus.HTTP_OK.getStatusCode()) {
                        result.put(connection.getResponseCode(), url);
                    } else {
                        result.put(connection.getResponseCode(), url);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                result.put(0, url);
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

    public Map<Integer, String> getResult() {
        return result;
    }
}
