
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
        boolean comparison;
        List<String> links = deadLinks.badLinks("https://android.stackexchange.com/questions/4538/can-i-emulate-a-bluetooth-keyboard-with-my-android-device");
        String ex = "{\n" +
                "  \"Your url:\" : \"https://android.stackexchange.com/questions/4538/can-i-emulate-a-bluetooth-keyboard-with-my-android-device\",\n" +
                "  \"Output\" : {\n" +
                "    \"total:\" : 258,\n" +
                "    \"dead:\" : 10\n" +
                "  },\n" +
                "  \"404\" : {\n" +
                "    \"urls:\" : [ [ \"https://stackexchange.com/users/?tab=inbox\", \"https://stackexchange.com/users/?tab=reputation\", \"https://android.stackexchange.com/posts/4538/edit\", \"https://android.stackexchange.com/posts/6483/edit\", \"https://market.android.com/details?id=org.pierre.remotedroid.client&feature=search_result\", \"https://android.stackexchange.com/posts/6484/edit\", \"https://android.stackexchange.com/posts/35842/edit\", \"https://android.stackexchange.com/posts/6518/edit\", \"https://android.stackexchange.com/posts/67698/edit\", \"https://android.stackexchange.com/questions/4538/can-i-emulate-a-bluetooth-keyboard-with-my-android-device?lastactivity\" ] ],\n" +
                "    \"size:\" : 10\n" +
                "  },\n" +
                "  \"50x\" : {\n" +
                "    \"urls:\" : [ [ ] ],\n" +
                "    \"size:\" : 0\n" +
                "  }\n" +
                "}";

         comparison = ex.equals(links);

        System.out.println(comparison);

    }
}
