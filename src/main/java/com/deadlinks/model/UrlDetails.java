package com.deadlinks.model;

import java.io.Serializable;
import java.util.List;

public class UrlDetails implements Serializable {

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

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
