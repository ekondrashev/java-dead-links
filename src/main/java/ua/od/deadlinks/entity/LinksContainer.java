package ua.od.deadlinks.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class LinksContainer {


    private String url;
    @SerializedName("404")
    private ResponseCode pageNotFoundResponse;
    @SerializedName("50x")
    private ResponseCode serverErrorResponse;
    @SerializedName("dead")
    private int deadLinksCount;
    @SerializedName("total")
    private int totalLinks;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResponseCode getPageNotFoundResponse() {
        return pageNotFoundResponse;
    }

    public void setPageNotFoundResponse(ResponseCode pageNotFoundResponse) {
        this.pageNotFoundResponse = pageNotFoundResponse;
    }

    public ResponseCode getServerErrorResponse() {
        return serverErrorResponse;
    }

    public void setServerErrorResponse(ResponseCode serverErrorResponse) {
        this.serverErrorResponse = serverErrorResponse;
    }

    public int getDeadLinksCount() {
        return deadLinksCount;
    }

    public void setDeadLinksCount(int deadLinksCount) {
        this.deadLinksCount = deadLinksCount;
    }

    public int getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(int totalLinks) {
        this.totalLinks = totalLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinksContainer that = (LinksContainer) o;
        return deadLinksCount == that.deadLinksCount &&
                totalLinks == that.totalLinks &&
                Objects.equals(url, that.url) &&
                Objects.equals(pageNotFoundResponse, that.pageNotFoundResponse) &&
                Objects.equals(serverErrorResponse, that.serverErrorResponse);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, pageNotFoundResponse, serverErrorResponse, deadLinksCount, totalLinks);
    }

    @Override
    public String toString() {
        return "LinksContainer{" +
                "url='" + url + '\'' +
                ", pageNotFoundResponse=" + pageNotFoundResponse +
                ", serverErrorResponse=" + serverErrorResponse +
                ", deadLinksCount=" + deadLinksCount +
                ", totalLinks=" + totalLinks +
                '}';
    }
}
