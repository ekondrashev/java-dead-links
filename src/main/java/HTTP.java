import com.google.gson.*;
import com.sun.org.apache.bcel.internal.util.ClassPath;
import org.json.JSONObject;

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
        private boolean enableRecording = Main.enableRecordingJson;
        private String recordingFileName = Main.JsonRecordingFileName;
        private JsonArray jsonArray = new JsonArray();

        {
            if (enableRecording){
                File fout;
                if ((fout = new File(recordingFileName)).exists()) {
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

                        if (enableRecording) {
                            jsonUrl.put("code", responseCode);
                            jsonUrl.put("asString", responseMessage);
                            jsonObject.put(url.toString(), jsonUrl);
                            recordJson(jsonObject);
                        }
                    } catch (IOException e) {
                        responseMessage = "Invalid URL";
                        if (enableRecording) {
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

                public void recordJson(JSONObject jsonObject){
                    JsonParser jsonParser = new JsonParser();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonElement el = jsonParser.parse(jsonObject.toString());
                    jsonArray.add(el);
                    String jsonOutput = gson.toJson(jsonArray);
                    try {
                        FileWriter fileWriter = new FileWriter(recordingFileName);
                        fileWriter.write(jsonOutput);
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }

}
