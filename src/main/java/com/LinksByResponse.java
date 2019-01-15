package com;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinksByResponse {

    private int size;
    private List <URL> links;

    public LinksByResponse() {
        links = new ArrayList<>();
    }

    public int getSize() {
        return size;
    }

    public void addLink(URL link) {
        links.add(link);
        size ++;

    }
}
