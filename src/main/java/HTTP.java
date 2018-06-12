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
                private int responseCode;
                private String responseMessage;

                @Override
                public int code() {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        responseCode = connection.getResponseCode();
                        responseMessage = connection.getResponseMessage();
                    } catch (IOException e) {
                        responseMessage = "Invalid URL";
                        return 404;
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
