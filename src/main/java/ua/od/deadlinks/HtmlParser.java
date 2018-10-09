package ua.od.deadlinks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {


    public List<URL> getLinks(String link)  {
        Document doc = getDocument(link);
        Elements elements = doc.getElementsByTag("a");
        List<URL> links = new ArrayList<>();
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
        String targetUrl = url.getProtocol() + url.getHost();
        elements.forEach(element -> {

            try {
                String value = element.attr("href").trim();
                if(value.startsWith("/")) {
                    value = targetUrl + value;
                } else if (value.startsWith("#")) {
                    value = link + "/" + value;
                }
                links.add(new URL(value));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        });
        return links;
    }


    private Document getDocument(String url){
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
