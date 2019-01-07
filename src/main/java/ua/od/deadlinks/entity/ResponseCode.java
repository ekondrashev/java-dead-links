package ua.od.deadlinks.entity;

import java.util.ArrayList;
import java.util.List;

public class ResponseCode {

    private int size;
    private List<String> urls;

    public ResponseCode() {
        urls = new ArrayList<>();
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

    public void addUrl(String url) {
        urls.add(url);
    }



}
