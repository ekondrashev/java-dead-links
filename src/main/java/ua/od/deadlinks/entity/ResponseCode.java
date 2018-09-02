package ua.od.deadlinks.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResponseCode {

    private int size;
    private List<URL> urls;

    public ResponseCode() {
        urls = new ArrayList<URL>();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public void addUrl(URL url) {
        urls.add(url);
    }



}
