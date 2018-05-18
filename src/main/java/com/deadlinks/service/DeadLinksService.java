package com.deadlinks.service;

import com.deadlinks.model.DeadLinksReport;
import com.deadlinks.model.UrlDetails;

import java.util.List;
import java.util.Map;

public class DeadLinksService {

    private HttpClient httpClient;
    private Map<Integer, List<String>> result;

    public DeadLinksService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public DeadLinksReport getDeadLinksReport() {
        result = httpClient.checkUrls();
        int totalLinks = countTotalLinks();
        int deadLinks = countDeadLinks();

        DeadLinksReport deadLinksReport = new DeadLinksReport();
        deadLinksReport.setUrl("");//TODO get url
        deadLinksReport.setUrlDetails(getUrlDetails());
        deadLinksReport.setDeadLinks(deadLinks);
        deadLinksReport.setTotalLinks(totalLinks);
        return deadLinksReport;
    }

    private int countTotalLinks() {
        return result.values().stream().mapToInt(List::size).sum();
    }

    private int countDeadLinks() {
        //TODO dead links
        return 0;
    }

    private UrlDetails getUrlDetails(){
        //TODO UrlDetails
        return null;
    }

}
