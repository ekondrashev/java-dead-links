import java.io.*;

public class FindDeadLinksApp {

  public static void main(String[] args) {
    //    DeadLinks deadLinks = new DeadLinks("https://jsoup.org");

    DeadLinks deadLinks = null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      String input = reader.readLine();
      deadLinks = new DeadLinks(input);
      deadLinks.findDeadLinks();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    System.out.println(deadLinks);
  }
}
