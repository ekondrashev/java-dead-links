package com.deadlinks;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.*;
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

                private int code;
                private String message;

                {
                    try {
                        openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public int code() {
                    try {
                        code = connection.getResponseCode();
                        asString();
                        if (Main.recordable) {
                            if (code == 404 || code >= 500) {
                                recordIntoFile();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return code;
                }

                @Override
                public String asString() {
                    message = connection.getContentType();
                    return message;
                }

                private void openConnection() throws IOException {
                    connection = (HttpURLConnection) url.openConnection();
                    if (connection == null) throw new RuntimeException("Connection not opened");
                }

                private ByteArrayOutputStream writableJson() throws IOException {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    JsonFactory jsonFactory = new JsonFactory();
                    JsonGenerator jsonGenerator = jsonFactory.createGenerator(stream, JsonEncoding.UTF8);
                    jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());

                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeObjectFieldStart(url.getPath());
                    jsonGenerator.writeStringField("code", String.valueOf(code));
                    jsonGenerator.writeStringField("asString", message);
                    jsonGenerator.writeEndObject();
                    jsonGenerator.writeEndObject();
                    jsonGenerator.writeRaw("\n");
                    jsonGenerator.close();
                    return stream;
                }

                private void recordIntoFile() throws IOException {
                    OutputStream out = new FileOutputStream(Main.PATHNAME, true);
                    ByteArrayOutputStream stream = writableJson();
                    stream.writeTo(out);
                    stream.close();
                    out.close();
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
