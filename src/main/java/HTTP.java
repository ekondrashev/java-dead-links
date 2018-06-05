import java.io.*;
import java.net.*;

interface HTTP {

  int code(URL url);

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
        return -1;
      }
    }
  }
}
