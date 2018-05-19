import java.io.*;

public class FindDeadLinksApp {

  public static void main(String[] args) {
    DeadLinks deadLinks = new DeadLinks(args[0]);
    try {
      deadLinks.findDeadLinks();
    } catch (IllegalArgumentException iae) {
      System.err.print("Illegal URL");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    System.out.println(deadLinks);
  }
}
