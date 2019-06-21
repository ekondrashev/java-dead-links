import com.google.gson.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
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
        private String recordingFilePath;
        private JsonArray jsonArray = new JsonArray();

        public Default(String recordingFilePath) {
            if (recordingFilePath != null) {
                this.recordingFilePath = recordingFilePath;

                File fout;
                if ((fout = new File(recordingFilePath)).exists()) {
                    fout.delete();
                }
            }
        }

        @Override
        public Response response(URL url) {
            return new Response() {
                private int responseCode;
                private String responseMessage;

                @Override
                public int code() {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonUrl = new JSONObject();
                    try {
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        responseCode = connection.getResponseCode();
                        responseMessage = connection.getResponseMessage();
                        if (recordingFilePath != null) {
                                jsonUrl.put("code", responseCode);
                                jsonUrl.put("asString", responseMessage);
                                jsonObject.put(url.toString(), jsonUrl);
                                recordJson(jsonObject);
                            }
                    } catch (IOException e) {
                        responseMessage = "Invalid URL";
                        if (recordingFilePath != null) {
                            jsonUrl.put("code", 404);
                            jsonUrl.put("asString", responseMessage);
                            jsonObject.put(url.toString(), jsonUrl);
                            recordJson(jsonObject);
                        }
                        return 404;
                    }
                    return responseCode;
                }

                @Override
                public String asString() {
                    return responseMessage;
                }

                private void recordJson(JSONObject jsonObject) {
                    JsonParser jsonParser = new JsonParser();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonElement el = jsonParser.parse(jsonObject.toString());
                    jsonArray.add(el);
                    String jsonOutput = gson.toJson(jsonArray);
                    try {
                        FileWriter writer = new FileWriter(recordingFilePath);
                        writer.write(jsonOutput);
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }

}
