import java.io.*;
import java.net.*;

interface HTTP {

  Response response(URL url);

  interface Response {

    int code();

    String asString();
  }

  class Default implements HTTP {

    private HttpURLConnection connection;

    @Override
    public Response response(URL url) {
      return new Response() {
        private String responseMessage;
        @Override
        public int code() {
          try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            this.responseMessage = connection.getResponseMessage();
            return connection.getResponseCode();
          } catch (IOException e) {
            this.responseMessage = e.getMessage();
            System.err.println("Connection Error:");
            System.err.println(e.getMessage());
            return -1;
          }
        }

        @Override
        public String asString() {
          return this.responseMessage;
        }
      };
    }

  }
}
