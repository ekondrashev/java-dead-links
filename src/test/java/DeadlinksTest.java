import java.io.*;
import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

@RunWith(JUnit4.class)
public class DeadlinksTest {

  @Before

  @Test
  public void main() throws IOException {
//    Deadlinks deadlinks = new Deadlinks("http://4pda.ru");
    Deadlinks deadlinks = new Deadlinks("https://jsoup.org");
    deadlinks.findDeadLinks();

  }


}
