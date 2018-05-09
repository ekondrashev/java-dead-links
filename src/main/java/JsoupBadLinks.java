import java.util.ArrayList;
import java.util.List;

import java.net.*;
import java.io.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupBadLinks {
    private static final int BAD_CODE = 404;
    private static final int BAD_CODE2 = 505;


    public int getResponseCode(String link) {
        URL url;
        HttpURLConnection con = null;
        Integer responsecode = 0;
        try {
            url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            responsecode = con.getResponseCode();
        } catch (Exception e) {
        } finally {
            if (null != con)
                con.disconnect();
        }
        return responsecode;

    }


    public List<String> allLinks(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        List<String> allLinks = new ArrayList<String>();
        for (Element link : links) {
            allLinks.add(link.attr("abs:href"));

        }
        allLinks.stream()
                .distinct();
        return allLinks;
    }


    public List<String> badLinks(String url) throws IOException {

        List<String> allUrls = allLinks(url);
        List<String> badUrls = new ArrayList<String>();
        List<String> badUrls505 = new ArrayList<String>();
        int i = 0;
        int b = 0;

        for (String linkk : allUrls) {
            int code = getResponseCode(linkk);
            if (code == BAD_CODE2) {
                b++;
                badUrls505.add(linkk);
            }

            if (code == BAD_CODE) {
                i++;
                badUrls.add(linkk);
            }
        }

        int c = i + b;
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(badUrls);

        JSONArray jsonArray505 = new JSONArray();
        jsonArray505.put(badUrls505);

        JSONObject outputJson = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        JSONObject jsonObject2 = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();

        try {
            outputJson.put("url:", url);
            jsonObject1.put("size:", i);
            jsonObject1.put("urls:", jsonArray);
            outputJson.put("404", jsonObject1);
            jsonObject2.put("size:", b);
            jsonObject2.put("urls:", jsonArray505);
            outputJson.put("505", jsonObject2);
            outputJson.put("dead:", c);
            outputJson.put("total:", allUrls.size());

            String s = outputJson.toString();

            Object jsonObject3 = mapper.readValue(s, Object.class);

            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject3));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return badUrls;
    }
}
