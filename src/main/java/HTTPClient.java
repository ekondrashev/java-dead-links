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

    public static void main(String[] args) {
        HTTPClient httpClient = new HTTPClient();
        System.out.println(httpClient.foundDeadLinks("https://mellivorasoft.com/"));
    }

    private static ArrayList<URL> getLinks(String url)
    {
        // Use Jsoup lib to get list of links from a page
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

    //The verifyUrl method verifies the url parameter passed as the input parameter by matching it with the regular expression.
    //If the match is successful, then it returns true; otherwise, it returns false.
    private boolean verifyUrl(String url) {
        String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher m = pattern.matcher(url);
        return m.matches();
    }

    public String foundDeadLinks(String url) {

        // Create local variable to store Response Codes
        Integer code;

        if (verifyUrl(url)) {
            try {
                // Use getLinks to return list of links from website specified at command-line
                ArrayList<URL> urls = getLinks(url);

                // Iterate through the Urls
                for (int i = 0; i < urls.size(); i++) {
                    try {
                        // Connect to the URL and add Response Code to Codes Array
                        HttpURLConnection connect = (HttpURLConnection) urls.get(i).openConnection();
                        code = connect.getResponseCode();

                        // If Response Code equal 404, then add this URL to ArrayList with code404
                        if (code == 404) {
                            code404.add(urls.get(i));
                        }
                        // If Response Code great or equal 500, then add this URL to ArrayList with code50x
                        else if (code >= 500){
                            code50x.add(urls.get(i));
                        }
                        else {
                            otherCodes.add(urls.get(i));
                        }
                        connect.disconnect();
                    }
                    // If the connection fails catch exception
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




