import java.io.*;

public class FindDeadLinksApp {

  public static void main(String[] args) {
    //    DeadLinks deadLinks = new DeadLinks("https://jsoup.org"); http://roofing.tilda.ws

    DeadLinks deadLinks = null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Please, input your url ");
    try {
      String input = reader.readLine();
      System.out.println("---");
      deadLinks = new DeadLinks(input);
      deadLinks.findDeadLinks();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    System.out.println(deadLinks);
  }
}
