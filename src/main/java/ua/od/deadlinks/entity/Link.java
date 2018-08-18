package ua.od.deadlinks.entity;

public class Link {

    private String link;
    private int code;

    public Link(String link, int code) {
        this.link = link;
        this.code = code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
