import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DeadLinksTests {

    @Test
    public void jsoupBadLinksTest() throws Exception {
        JsoupBadLinks deadLinks = new JsoupBadLinks();
        List<String> links = deadLinks.badLinks("https://android.stackexchange.com/questions/4538/can-i-emulate-a-bluetooth-keyboard-with-my-android-device");
        int result = 10;
        assertEquals(result, links.size());
    }

    @Test
    public void outputJsonTest() throws Exception {
        JsoupBadLinks deadLinks = new JsoupBadLinks();
        List<String> links = deadLinks.badLinks("https://android.stackexchange.com/questions/4538/can-i-emulate-a-bluetooth-keyboard-with-my-android-device");
        String ex = "{\n" +
                "  \"total:\" : ,\n" +
                "  \"dead:\" : ,\n" +
                "  \"404\" : {\n" +
                "    \"urls:\" : [ [ \"\" ] ],\n" +
                "    \"size:\" : \n" +
                "  },\n" +
                "  \"505\" : {\n" +
                "    \"urls:\" : [ [ ] ],\n" +
                "    \"size:\" : \n" +
                "  },\n" +
                "  \"url:\" : \"\"\n" +
                "}";

        String jsonExample = links.toString();

        ex.equals(jsonExample);

    }
}
