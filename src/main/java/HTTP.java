import java.io.*;
import java.net.*;

interface HTTP {

    Response response(URL url);

    interface Response {
        int code();
        String asString();
        //todo:
        void recordIntoJson();
    }

  class Default implements HTTP {

    private HttpURLConnection connection;
    private String responseMessage;

    @Override
    public Response response(URL url) {
      return new Response() {
        @Override
        public int code() {
          try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            responseMessage = connection.getContentType();
            return connection.getResponseCode();
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

        @Override
        public void recordIntoJson() {
          if (Main.recordingJson == null) {
            System.out.println("Recording into the file is disabled");
            return;
          }
          File fileRec = new File(Main.recordingJson);
          try (FileWriter fileWriter = new FileWriter(fileRec, false)) {
            fileWriter.write("Some string");
            fileWriter.flush();
          } catch (IOException ioe) {
            System.err.println(ioe.toString());
          }
        }
      };
    }
  }
}
