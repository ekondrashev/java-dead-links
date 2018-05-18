package com.deadlinks.service;

import com.deadlinks.model.DeadLinksReport;
import com.deadlinks.model.UrlDetails;

import java.util.HashMap;
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
        deadLinksReport.setUrlDetails(getUrlDetails());
        deadLinksReport.setDeadLinks(deadLinks);
        deadLinksReport.setTotalLinks(totalLinks);
        return deadLinksReport;
    }

    private int countTotalLinks() {
        return result.values().stream().mapToInt(List::size).sum();
    }

    private int countDeadLinks() {
        AtomicInteger deadLinks = new AtomicInteger();
        result.forEach((statusCode, listOfLinks) -> {
            if (statusCode >= 400 || statusCode == 0) {
                deadLinks.addAndGet(listOfLinks.size());
            }
        });
        return deadLinks.intValue();
    }

    private Map<Integer, UrlDetails> getUrlDetails(){
        Map<Integer, UrlDetails> urlDetails = new HashMap<>();
        result.forEach((statusCode, listOfLinks) -> {
            if (statusCode >= 400 || statusCode == 0) {
                urlDetails.putIfAbsent(statusCode, new UrlDetails(listOfLinks.size(), listOfLinks));
            }
        });
        return urlDetails;
    }

    private String getUrl() {
       return httpClient.getUrls()[0];
    }

}
