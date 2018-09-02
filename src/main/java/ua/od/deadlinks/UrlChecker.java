package ua.od.deadlinks;


import com.google.gson.GsonBuilder;
import ua.od.deadlinks.entity.Link;
import ua.od.deadlinks.entity.LinksContainer;
import ua.od.deadlinks.entity.ResponseCode;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlChecker {

    private String targetUrl;

    public UrlChecker(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String checkUrls(List<URL> urls) {

        List<Link> urlsResponseCode = getUrlsResponseCode(urls);

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
        container.setTotalLinks(urls.size());
        container.setUrl(targetUrl);
        container.setPageNotFoundResponse(resp404);
        container.setServerErrorResponse(resp50x);

        return new GsonBuilder().setPrettyPrinting()
                .create()
                .toJson(container);
    }


    public List<Link> getUrlsResponseCode(List<URL> links)  {
        List<Link> linksWithResponse = new ArrayList<>();
        for (URL link : links) {
            linksWithResponse.add(new Link(link, getResponseCode(link)));
        }
        return linksWithResponse;
    }


    private int getResponseCode(URL url) {
        HttpURLConnection connection = null;
        int code = 200;
        if(url.toString().length() == 0) {
            return 404;
        }

        try {
            if(url.getProtocol().equals("https")) {
                connection = (HttpsURLConnection) url.openConnection();
            } else if (url.getProtocol().equals("http")){
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setConnectTimeout(5000);
            code = connection.getResponseCode();
        } catch (IOException e) {
            code = 404;
        } finally {
            if(connection != null) connection.disconnect();
        }

        return code;
    }

}
