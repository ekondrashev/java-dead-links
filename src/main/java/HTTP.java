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
            };
        }
    }
}
