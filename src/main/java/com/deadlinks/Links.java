package com.deadlinks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface Links extends Iterable<URL> {
    @Override
    Iterator<URL> iterator();

    class HTML implements Links {

        private static final int HTTP_OK = 200;
        private static final int NOT_FOUND = 404;
        private static final int SERVER_ERROR = 500;

        private String arg;
        private List<URL> urls;
        private Map<Integer, List<String>> result;
        private List<String> linksFromHtml;
        private DeadLinksReport deadLinksReport;
        private HTTP http;

        public HTML(String arg, HTTP http) {
            super();
            this.arg = arg;
            this.http = http;
            result = new HashMap<>();
            linksFromHtml = getLinksFromHtml();
            deadLinksReport = getDeadLinksReport();
        }

        @Override
        public Iterator<URL> iterator() {
            List<String> incorrectUrls = new ArrayList<>();
            urls = new ArrayList<>();
            linksFromHtml.stream().forEach(link -> {
                if (verifyUrl(link)) {
                    try {
                        urls.add(new URL(link));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    incorrectUrls.add(link);
                    result.putIfAbsent(0, incorrectUrls);
                }
            });
            return urls.iterator();
        }

        public DeadLinksReport getDeadLinksReport() {
            result = checkUrls();
            int totalLinks = countTotalLinks();
            int deadLinks = countDeadLinks();

            DeadLinksReport deadLinksReport = new DeadLinksReport();
            deadLinksReport.setUrl(arg);
            UrlDetails urlDetails404 = getUrlDetails404();
            if (urlDetails404 != null) {
                deadLinksReport.setUrlDetails400(getUrlDetails404());
            } else {
                deadLinksReport.setUrlDetails400(new UrlDetails(0, Collections.emptyList()));
            }

            UrlDetails urlDetails50x = getUrlDetails50x();
            if (urlDetails50x != null) {
                deadLinksReport.setUrlDetails50x(getUrlDetails50x());
            } else {
                deadLinksReport.setUrlDetails50x(new UrlDetails(0, Collections.emptyList()));
            }

            deadLinksReport.setDead(deadLinks);
            deadLinksReport.setTotal(totalLinks);
            return deadLinksReport;
        }

        private Map<Integer, List<String>> checkUrls() {
            List<String> successUrls = new ArrayList<>();
            List<String> failedUrls = new ArrayList<>();

            Iterator<URL> urlIterator = iterator();
            while (urlIterator.hasNext()) {
                URL url = urlIterator.next();
                int code = http.response(url).code();
                if (code == HTTP_OK) {
                    successUrls.add(url.getPath());
                    result.putIfAbsent(code, successUrls);
                } else {
                    failedUrls.add(url.getPath());
                    result.putIfAbsent(code, failedUrls);
                }
            }
            return result;
        }

        private List<String> getLinksFromHtml() {
            List<String> linkList = new ArrayList<>();
            try {
                Document page = Jsoup.connect(arg).get();
                Elements links = page.select("a[href]");
                for (Element link : links) {
                    linkList.add(link.attr("href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return linkList;
        }

        private boolean verifyUrl(String url) {
            String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
            Pattern pattern = Pattern.compile(urlRegex);
            Matcher matcher = pattern.matcher(url);
            return matcher.matches();
        }

        private int countTotalLinks() {
            return result.values().stream().mapToInt(List::size).sum();
        }

        private int countDeadLinks() {
            AtomicInteger deadLinks = new AtomicInteger();
            result.forEach((statusCode, listOfLinks) -> {
                if (statusCode == NOT_FOUND || statusCode >= SERVER_ERROR) {
                    deadLinks.addAndGet(listOfLinks.size());
                }
            });
            return deadLinks.intValue();
        }

        private UrlDetails getUrlDetails404() {
            final UrlDetails[] urlDetails = {null};
            result.forEach((statusCode, listOfLinks) -> {
                if (statusCode == NOT_FOUND) {
                    urlDetails[0] = new UrlDetails(listOfLinks.size(), listOfLinks);
                }
            });
            return urlDetails[0];
        }

        private UrlDetails getUrlDetails50x() {
            final UrlDetails[] urlDetails = {null};
            result.forEach((statusCode, listOfLinks) -> {
                if (statusCode >= SERVER_ERROR) {
                    urlDetails[0] = new UrlDetails(listOfLinks.size(), listOfLinks);
                }
            });
            return urlDetails[0];
        }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String valueAsString = "";
            try {
                valueAsString = mapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(deadLinksReport);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return valueAsString;
        }
    }

    class DeadLinksReport implements Serializable {

        private static final long serialVersionUID = 1234L;

        private String url;
        @JsonProperty(value = "404")
        private UrlDetails urlDetails400;
        @JsonProperty(value = "50x")
        private UrlDetails urlDetails50x;
        private int dead;
        private int total;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public UrlDetails getUrlDetails400() {
            return urlDetails400;
        }

        public void setUrlDetails400(UrlDetails urlDetails400) {
            this.urlDetails400 = urlDetails400;
        }

        public UrlDetails getUrlDetails50x() {
            return urlDetails50x;
        }

        public void setUrlDetails50x(UrlDetails urlDetails50x) {
            this.urlDetails50x = urlDetails50x;
        }

        public int getDead() {
            return dead;
        }

        public void setDead(int dead) {
            this.dead = dead;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }


    class UrlDetails implements Serializable {

        private static final long serialVersionUID = 3214L;

        private int size;
        private List<String> urls;

        public UrlDetails(int size, List<String> urls) {
            this.size = size;
            this.urls = urls;
        }

        public int getSize() {
            return size;
        }

        public List<String> getUrls() {
            return urls;
        }
    }
}