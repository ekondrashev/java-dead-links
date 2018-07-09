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

        private String pathToFile;

        public Default() { }

        public Default(String pathToFile) {
            this.pathToFile = pathToFile;
        }

        @Override
        public Response response(URL url) {
            return new Response() {
                private Integer responseCode;
                private String responseMessage;

                @Override
                public int code() {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        responseCode = connection.getResponseCode();
                        responseMessage = connection.getResponseMessage();
                        if (pathToFile != null) {
                            recordResponsesIntoFile(pathToFile);
                        }
                        connection.disconnect();
                    } catch (IOException e) {
                        responseMessage = e.getMessage();
                        System.err.println(e.getMessage());
                        return -1;
                    }
                    return responseCode;
                }

                @Override
                public String asString() {
                    return responseMessage;
                }

                private void recordResponsesIntoFile(String pathToFile) {
                    JSONOrderedObject recordingObj = new JSONOrderedObject();
                    JSONOrderedObject urlObj = new JSONOrderedObject();
                    urlObj.put("code" , responseCode);
                    urlObj.put("asString", responseMessage);
                    recordingObj.put(url.toString(), urlObj);
                    String jsonFile = new PrintResultInJsonFormat().convertToPrettyJSONUtility(recordingObj);
                    try {
                        Writer writer = new FileWriter(pathToFile);
                        writer.write(jsonFile);
                        writer.flush();
                    } catch (IOException e) {
                        System.err.println(e.toString());
                    }
                }
            };
        }
    }
}
