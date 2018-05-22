package com.deadlinks.service;

import com.deadlinks.model.DeadLinksReport;
import com.deadlinks.model.UrlDetails;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
        deadLinksReport.setUrl(getUrl());
        UrlDetails urlDetails404 = getUrlDetails404();
        if (urlDetails404 != null) {
            deadLinksReport.setUrlDetails400(getUrlDetails404());
        } else {
            deadLinksReport.setUrlDetails400(new UrlDetails(0, Collections.emptyList()));
        }

        UrlDetails urlDetails50x = getUrlDetails50x();
        if (urlDetails50x != null) {
            deadLinksReport.setUrlDetails50x(getUrlDetails50x());
        } else {
            deadLinksReport.setUrlDetails50x(new UrlDetails(0, Collections.emptyList()));
        }

        deadLinksReport.setDead(deadLinks);
        deadLinksReport.setTotal(totalLinks);
        return deadLinksReport;
    }

    private int countTotalLinks() {
        return result.values().stream().mapToInt(List::size).sum();
    }

    private int countDeadLinks() {
        AtomicInteger deadLinks = new AtomicInteger();
        result.forEach((statusCode, listOfLinks) -> {
            if (statusCode == 404 || statusCode >= 500) {
                deadLinks.addAndGet(listOfLinks.size());
            }
        });
        return deadLinks.intValue();
    }

    private UrlDetails getUrlDetails404(){
        final UrlDetails[] urlDetails = {null};
        result.forEach((statusCode, listOfLinks) -> {
            if (statusCode == 404) {
                urlDetails[0] = new UrlDetails(listOfLinks.size(), listOfLinks);
            }
        });
        return urlDetails[0];
    }

    private UrlDetails getUrlDetails50x(){
        final UrlDetails[] urlDetails = {null};
        result.forEach((statusCode, listOfLinks) -> {
            if (statusCode >= 500) {
                urlDetails[0] = new UrlDetails(listOfLinks.size(), listOfLinks);
            }
        });
        return urlDetails[0];
    }

    private String getUrl() {
       return httpClient.getUrls()[0];
    }

}
