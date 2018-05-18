package com.deadlinks.model;

import java.io.Serializable;
import java.util.Map;

public class DeadLinksReport implements Serializable {

    private static final long serialVersionUID = 1234L;

    private String url;
    private Map<Integer, UrlDetails> urlDetails;
    private int deadLinks;
    private int totalLinks;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<Integer, UrlDetails> getUrlDetails() {
        return urlDetails;
    }

    public void setUrlDetails(Map<Integer, UrlDetails> urlDetails) {
        this.urlDetails = urlDetails;
    }

    public int getDeadLinks() {
        return deadLinks;
    }

    public void setDeadLinks(int deadLinks) {
        this.deadLinks = deadLinks;
    }

    public int getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(int totalLinks) {
        this.totalLinks = totalLinks;
    }
}
