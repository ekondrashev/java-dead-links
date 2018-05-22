package com.deadlinks.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DeadLinksReport implements Serializable {

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
