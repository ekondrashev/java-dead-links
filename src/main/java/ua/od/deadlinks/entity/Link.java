package ua.od.deadlinks.entity;

import java.net.URL;

public class Link {

    private URL link;
    private int code;

    public Link(URL link, int code) {
        this.link = link;
        this.code = code;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}