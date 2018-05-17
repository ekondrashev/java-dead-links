package com.deadlinks;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();
        httpClient.checkUrls(args);
        System.out.println(httpClient.getResult());
    }
}
