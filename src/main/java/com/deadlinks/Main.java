package com.deadlinks;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
            String url = new String(args[0]);
            try {
                HttpClient client = new HttpClient(url);
            } catch (IOException e) {

            }
    }
}
