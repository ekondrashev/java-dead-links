package ua.od.deadlinks;


import com.google.gson.GsonBuilder;
import ua.od.deadlinks.entity.Link;
import ua.od.deadlinks.entity.LinksContainer;
import ua.od.deadlinks.entity.ResponseCode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlChecker {

    private String targetUrl;

    public UrlChecker(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String checkUrls(){
        List<String> links = new HtmlParser().getLinks(targetUrl);
        List<Link> urlsResponseCode = getUrlsResponseCode(links);

        LinksContainer container = new LinksContainer();
        ResponseCode resp404 = new ResponseCode();
        ResponseCode resp50x = new ResponseCode();

        int count404 = 0;
        int count50x = 0;

        for (Link link: urlsResponseCode) {
            if(link.getCode() == 404){
                count404++;
                resp404.addUrl(link.getLink());
            } else if(link.getCode() >= 500 && link.getCode() < 510) {
                count50x++;
                resp50x.addUrl(link.getLink());
            }
        }

        resp404.setSize(count404);
        resp50x.setSize(count50x);
        container.setDeadLinksCount(resp50x.getSize() + resp404.getSize());
        container.setTotalLinks(links.size());
        container.setUrl(targetUrl);
        container.setPageNotFoundResponse(resp404);
        container.setServerErrorResponse(resp50x);

        return new GsonBuilder().setPrettyPrinting()
                .create()
                .toJson(container);
    }


    public List<Link> getUrlsResponseCode(List<String> links) {
        List<Link> linksWithResponse = new ArrayList<>();
        for (String link : links) {
            linksWithResponse.add(new Link(link.trim(), getResponseCode(link.trim())));
        }
        return linksWithResponse;
    }


    private int getResponseCode(String link) {
        URL url;
        HttpURLConnection connection = null;
        int code = 200;
        if(link.length() == 0) {
            return 404;
        }

        if(link.startsWith("/")) {
            link = targetUrl + link;
        } else if (link.startsWith("#")) {
            link = targetUrl + "/" + link;
        }

        try {
            url = new URL(link);
        if(link.startsWith("https")) {
            connection = (HttpsURLConnection) url.openConnection();
        } else if (link.startsWith("http")){
            connection = (HttpURLConnection) url.openConnection();
        }
             code = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) connection.disconnect();
        }

        return code;
    }

}
