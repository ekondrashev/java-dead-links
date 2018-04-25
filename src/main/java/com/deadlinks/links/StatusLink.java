package com.deadlinks.links;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public final class StatusLink  implements Link{
    private String url;
    private String statusMessage;
    private int statusCode;
//    private Connection.Response response;
    private HttpURLConnection connection;
//    private URLConnection connection;

    private void retrieveStatus() {
        try {
            this.statusCode = this.connection.getResponseCode();
            this.statusMessage = this.connection.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: filter out mailto
    private void connect() {
        try {
            URL urlNew = new URL(this.url);
            System.out.println(this.url);
            this.connection = (HttpURLConnection) urlNew.openConnection();
            connection.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String url() {
        return this.url;
    }

//    @Override
//    public Connection.Response response() {
//        return this.response;
//    }

    public String statusMsg() {
        return this.statusMessage;
    }

    public int statusCode() {
        return this.statusCode;
    }


    public StatusLink(String url) {
        this.url = url;
        connect();
        retrieveStatus();
    }

}
