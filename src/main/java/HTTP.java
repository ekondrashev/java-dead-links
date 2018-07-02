import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

interface HTTP {

    Response response(URL url);

    interface Response {
        int code();

        String asString();

    }

    class Default implements HTTP {

        private HttpURLConnection connection;
        private int responseCode;
        private String responseMessage;
        private String recordPath;

        public Default(String recordPath) {
            this.recordPath = recordPath;
        }

        @Override
        public Response response(URL url) {
            return new Response() {
                @Override
                public int code() {
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                        responseMessage = connection.getContentType().split(";")[0];
                        responseCode = connection.getResponseCode();
                        if (Main.recordingJson != null) {
                            this.recordIntoJson();
                        }
                        return responseCode;
                    } catch (IOException e) {
                        responseMessage = e.getMessage();
                        System.err.println("Connection Error:");
                        System.err.println(e.getMessage());
                        return -1;
                    }
                }

                @Override
                public String asString() {
                    return responseMessage;
                }

                private void recordIntoJson() {
                    try (Writer writer = new FileWriter(recordPath + Main.recordingJson)) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonObject recordingObject = new JsonObject();
                        JsonObject urlJson = new JsonObject();
                        urlJson.addProperty("code", responseCode);
                        urlJson.addProperty("asString", responseMessage);
                        recordingObject.add(url.toString(), urlJson);
                        gson.toJson(recordingObject, writer);
                    } catch (IOException ioe) {
                        System.err.println(ioe.toString());
                    }
                }
            };
        }

        public String getRecordPath() {
            return recordPath;
        }

        public void setRecordPath(String recordPath) {
            this.recordPath = recordPath;
        }
    }
}
