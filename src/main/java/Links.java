import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface Links extends Iterable<URL> {
    class HTML implements Links {

        private ArrayList<URL> code404 = new ArrayList<>();
        private ArrayList<URL> code50x = new ArrayList<>();
        private ArrayList<URL> otherCodes = new ArrayList<>();
        private String url;

        public HTML(String url) {
            this.url = url;
        }

        private static ArrayList<URL> getLinks(String url)
        {
            ArrayList<URL> urls = new ArrayList<>();
            try {
                Document document = Jsoup.connect(url).get();
                Elements links = document.select("a[href]");
                for (Element link : links) {
                    URL tmpUrl = new URL(link.attr("abs:href"));
                    urls.add(tmpUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return urls;
        }

        private boolean verifyUrl(String url) {
            String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
            Pattern pattern = Pattern.compile(urlRegex);
            Matcher m = pattern.matcher(url);
            return m.matches();
        }

        @Override
        public String toString() {

            if (verifyUrl(url)) {
                try {
                    Integer code;

                    for (Iterator<URL> iter = iterator(); iter.hasNext(); ) {
                        URL link = iter.next();
                        try {
                            HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                            code = connect.getResponseCode();

                            if (code == 404) {
                                code404.add(link);
                            }
                            else if (code >= 500){
                                code50x.add(link);
                            }
                            else {
                                otherCodes.add(link);
                            }
                            connect.disconnect();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    String s = e.getMessage();
                    if (s != null) {
                        System.out.println(s);
                    }
                }
            } else {
                return "You input incorrect URL";
            }
            PrintResultInJson printResult = new PrintResultInJson();
            return printResult.createJson(url, code404, code50x, otherCodes);
        }

        public Iterator<URL> iterator() {
            ArrayList<URL> urls = getLinks(url);
            return urls.iterator();
        }
    }

    class PrintResultInJson {

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

        private String convertToPrettyJSONUtility(JSONOrderedObject simpleJSON) {

            String jsonPrettyStr = simpleJSON.toJSONString();

            try {
                ObjectMapper mapperObj = new ObjectMapper();
                jsonPrettyStr = mapperObj.writerWithDefaultPrettyPrinter().writeValueAsString(simpleJSON);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonPrettyStr;
        }

        class JSONOrderedObject extends LinkedHashMap<String, Object> implements Map<String, Object>, JSONAware, JSONStreamAware {

            @Override
            public String toJSONString() {
                return JSONObject.toJSONString(this);}

            @Override
            public void writeJSONString(Writer writer) throws IOException {
                JSONObject.writeJSONString(this, writer);}
        }
    }
}


