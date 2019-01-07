package ua.od.deadlinks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {


    public List<String> getLinks(String url){
        Document doc = getDocument(url);
        Elements elements = doc.getElementsByTag("a");
        List<String> links = new ArrayList<>();
        elements.forEach(element -> links.add(element.attr("href")));
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
