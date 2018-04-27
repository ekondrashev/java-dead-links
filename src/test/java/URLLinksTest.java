import com.deadlinks.links.URLLinks;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class URLLinksTest {

    @Test
    public void testURLStringsShouldThrowMalformedURLException() throws MalformedURLException {
        URLLinks tester = new URLLinks("Roses are red, URLs are blue");
        assertEquals(tester.parentState ,"Malformed URL.");
    }

}
