import java.net.URL;

interface HTTP {

    Response response(URL url);

    interface Response {
        int code();
        String asString();
    }

}


/*
class Default implements HTTP {

    private HttpURLConnection connection;

    @Override
    public int code(URL url) {
      try {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getResponseCode();
      } catch (IOException e) {
        System.err.println("Connection Error:");
        System.err.println(e.getMessage());
        return -1;
      }
    }
  }
 */