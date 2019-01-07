package com;

import java.util.ArrayList;
import java.util.List;

public class LinksByResponse {

    private int size;
    private List <String> links;

    public LinksByResponse() {
        links = new ArrayList<>();
    }

    public int getSize() {
        return size;
    }

    public void addLink(String link) {
        links.add(link);
        size ++;

    }
}
