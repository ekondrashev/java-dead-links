import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

class PrintResultInJsonFormat {

    @SuppressWarnings("unchecked")
    String createJson(String url, ArrayList<URL> code404, ArrayList<URL> code50x, ArrayList<URL> otherCodes) {

        JSONOrderedObject obj = new JSONOrderedObject();

        obj.put("url", url);

        if (!code404.isEmpty()) {
            JSONArray array404 = new JSONArray();
            array404.addAll(code404);
            JSONOrderedObject obj404 = new JSONOrderedObject();
            obj404.put("size", code404.size());
            obj404.put("urls", array404);
            obj.put("404", obj404);
        }

        if (!code50x.isEmpty()) {
            JSONArray array50x = new JSONArray();
            array50x.addAll(code50x);
            JSONOrderedObject obj50x = new JSONOrderedObject();
            obj50x.put("size", code50x.size());
            obj50x.put("urls", array50x);
            obj.put("50x", obj50x);
        }

        obj.put("dead", code404.size() + code50x.size());
        obj.put("total", code404.size() + code50x.size() + otherCodes.size());

        return convertToPrettyJSONUtility(obj);
    }

    protected String convertToPrettyJSONUtility(JSONOrderedObject simpleJSON) {

        String jsonPrettyStr = simpleJSON.toJSONString();

        try {
            ObjectMapper mapperObj = new ObjectMapper();
            jsonPrettyStr = mapperObj.writerWithDefaultPrettyPrinter().writeValueAsString(simpleJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonPrettyStr;
    }
}

