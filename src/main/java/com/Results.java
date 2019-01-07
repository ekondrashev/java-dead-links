package com;

import com.google.gson.annotations.SerializedName;

public class Results {
    private String url;
    @SerializedName("404")
    private LinksByResponse notFound;
    @SerializedName("50x")
    private LinksByResponse serverError;
    @SerializedName("dead")
    private int deadLinksQuantity;
    @SerializedName("total")
    private int totalLinks;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinksByResponse getNotFound() {
        return notFound;
    }

    public void setNotFound(LinksByResponse notFound) {
        this.notFound = notFound;
    }

    public LinksByResponse getServerError() {
        return serverError;
    }

    public void setServerError(LinksByResponse serverError) {
        this.serverError = serverError;
    }

    public int getDeadLinksQuantity() {
        return deadLinksQuantity;
    }

    public void setDeadLinksQuantity(int deadLinksQuantity) {
        this.deadLinksQuantity = deadLinksQuantity;
    }

    public int getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(int totalLinks) {
        this.totalLinks = totalLinks;
    }



}
