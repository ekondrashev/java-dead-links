package com;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface HTTP {
    Response response(URL url);

    interface Response {
        int code();

        String asString();
    }


    class Default implements HTTP {

        @Override
        public Response response(URL url) {
            return new Response() {
                private int responseCode;
                private String responseMessage;

                @Override
                public int code() {
                    HttpURLConnection connection = null;
                    int responseCode = 200;
                    if (url.toString().length() == 0) {
                        return 404;
                    }
                    try {
                        if (url.getProtocol().equals("https")) {
                            connection = (HttpsURLConnection) url.openConnection();
                        } else if (url.getProtocol().equals("http")) {
                            connection = (HttpURLConnection) url.openConnection();
                        }
                        connection.setConnectTimeout(5000);
                        responseCode = connection.getResponseCode();
                    } catch (IOException e) {
                        responseCode = 404;
                    } finally {
                        if (connection != null) connection.disconnect();
                    }
                    return responseCode;
                }

                @Override
                public String asString() {
                    return responseMessage;
                }
            };
        }
    }
}

