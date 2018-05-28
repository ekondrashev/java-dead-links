import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPClient {

    private ArrayList<URL> code404 = new ArrayList<>();
    private ArrayList<URL> code50x = new ArrayList<>();
    private ArrayList<URL> otherCodes = new ArrayList<>();
    private String url;

    public HTTPClient(String url) {
        this.url = url;
    }

    public static void main(String[] args) {
        HTTPClient httpClient = new HTTPClient(args[0]);
        System.out.print(httpClient.toString());
    }

    private static ArrayList<URL> getLinks(String url)
    {
        ArrayList<URL> urls = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");
            for (Element link : links) {
                URL tmpUrl = new URL(link.attr("abs:href").toString());
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
        Integer code;

        if (verifyUrl(url)) {
            try {
                ArrayList<URL> urls = getLinks(url);

                for (int i = 0; i < urls.size(); i++) {
                    try {
                        HttpURLConnection connect = (HttpURLConnection) urls.get(i).openConnection();
                        code = connect.getResponseCode();

                        if (code == 404) {
                            code404.add(urls.get(i));
                        }
                        else if (code >= 500){
                            code50x.add(urls.get(i));
                        }
                        else {
                            otherCodes.add(urls.get(i));
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
}




