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
    private static final int BAD_CODE0 = 500;
    private static final int BAD_CODE1 = 501;
    private static final int BAD_CODE2 = 502;
    private static final int BAD_CODE3 = 503;
    private static final int BAD_CODE4 = 504;
    private static final int BAD_CODE5 = 505;


    public int getResponseCode(String link) {
        URL url;
        HttpURLConnection con = null;
        Integer responsecode = 0;
        try {
            url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            responsecode = con.getResponseCode();
        } catch (Exception e) {
            System.err.print(Exception.class);
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
        List<String> badUrls50x = new ArrayList<String>();

        for (String links : allUrls) {
            int code = getResponseCode(links);
            if (code == BAD_CODE0) {
                badUrls50x.add(links);
            }
            if (code == BAD_CODE1) {
                badUrls50x.add(links);
            }
            if (code == BAD_CODE2) {
                badUrls50x.add(links);
            }
            if (code == BAD_CODE3) {
                badUrls50x.add(links);
            }
            if (code == BAD_CODE4) {
                badUrls50x.add(links);
            }
            if (code == BAD_CODE5) {
                badUrls50x.add(links);
            }
            if (code == BAD_CODE) {
                badUrls.add(links);
            }
        }
        JSONObject outputJson = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(badUrls);

        JSONArray jsonArray505 = new JSONArray();
        jsonArray505.put(badUrls50x);

        JSONObject jsonObject1 = new JSONObject();

        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();

        try {
            jsonObject3.put("dead:", badUrls.size()+badUrls50x.size());
            jsonObject3.put("total:", allUrls.size());
            outputJson.put("Output", jsonObject3);




            jsonObject1.put("size:", badUrls.size());
            jsonObject1.put("urls:", jsonArray);
            outputJson.put("404", jsonObject1);

            jsonObject2.put("size:", badUrls50x.size());
            jsonObject2.put("urls:", jsonArray505);
            outputJson.put("50x", jsonObject2);
            outputJson.put("Your url:", url);


            String s = outputJson.toString();

            Object jsonObject4 = mapper.readValue(s, Object.class);

            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject4));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return badUrls;
    }
}
