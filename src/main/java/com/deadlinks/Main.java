package com.deadlinks;

import com.deadlinks.model.DeadLinksReport;
import com.deadlinks.service.DeadLinksService;
import com.deadlinks.service.HttpClient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        HttpClient httpClient = new HttpClient(args);
        DeadLinksService deadLinksService = new DeadLinksService(httpClient);
        DeadLinksReport deadLinksReport = deadLinksService.getDeadLinksReport();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deadLinksReport));
    }
}
