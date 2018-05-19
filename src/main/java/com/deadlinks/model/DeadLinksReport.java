package com.deadlinks.model;

import java.io.Serializable;
import java.util.Map;

public class DeadLinksReport implements Serializable {

    private static final long serialVersionUID = 1234L;

    private String url;
    private Map<Integer, UrlDetails> urlDetails;
    private int dead;
    private int total;

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
