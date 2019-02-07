package com;

import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


interface Links extends Iterable<URL> {

    class HTML implements Links {
        private String pageUrl;
        private HTTP http;

        public HTML(String pageUrl, HTTP http) {
            this.pageUrl = pageUrl;
            this.http = http;
        }

        public String toString() {
            LinksByResponse links404 = new LinksByResponse();
            LinksByResponse links50X = new LinksByResponse();
            Results results = new Results();
            int count = 0;

            for (URL link : this) {
                HTTP.Response response = http.response(link);
                int code = response.code();
                count++;
                if (code == 404) {
                    links404.addLink(link);
                } else if (code >= 500 && code < 510) {
                    links50X.addLink(link);
                }
            }
            results.setUrl(pageUrl);
            results.setNotFound(links404);
            results.setServerError(links50X);
            results.setDeadLinksQuantity(links50X.getSize() + links404.getSize());
            results.setTotalLinks(count);
            return new GsonBuilder().setPrettyPrinting().create().toJson(results);
        }

        public ArrayList<URL> linksParser(String inputUrl) {
            try {
                Document document = Jsoup.connect(inputUrl).get();
                Elements elements = document.getElementsByTag("a");
                ArrayList<URL> urls = new ArrayList<>();
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
                return urls;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Iterator<URL> iterator() {
            ArrayList<URL> urls = linksParser(pageUrl);
            return urls.iterator();
        }
    }
}
