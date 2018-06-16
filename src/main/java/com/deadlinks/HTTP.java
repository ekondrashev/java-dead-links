package com.deadlinks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

interface HTTP {

    Response response(URL url);

    interface Response {
        int code();

        String asString();
    }

    class Default implements HTTP {
        HttpURLConnection connection;

        public Default() {
            Runtime.getRuntime().addShutdownHook(new CloseConnection());
        }

        @Override
        public Response response(URL url) {
            return new Response() {

                {
                    try {
                        openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public int code() {
                    int code = 0;
                    try {
                        code = connection.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (code == 0) throw new RuntimeException("Invalid link");
                    return code;
                }

                @Override
                public String asString() {
                    String message = "";
                    try {
                        message = connection.getResponseMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return message;
                }

                private void openConnection() throws IOException {
                    connection = (HttpURLConnection) url.openConnection();
                    if (connection == null) throw new RuntimeException("Connection not opened");
                }
            };
        }

        private class CloseConnection extends Thread {
            @Override
            public void run() {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

    }

}
