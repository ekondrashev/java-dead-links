import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface Links extends Iterable<URL> {
    class HTML implements Links {
        private ArrayList<URL> code404;
        private ArrayList<URL> code50x;
        private ArrayList<URL> otherCodes;
        private String url;
        private HTTP http;

        public HTML(String url, HTTP http) {
            this.code404 = new ArrayList<>();
            this.code50x = new ArrayList<>();
            this.otherCodes = new ArrayList<>();
            this.url = url;
            this.http = http;
        }

        private static ArrayList<URL> returnLinksFromHTML(String url)
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
                for (URL link : this) {
                    HTTP.Response response = http.response(link);
                    Integer code = response.code();

                    if (code == 404) {
                        code404.add(link);
                    } else if (code >= 500) {
                        code50x.add(link);
                    } else {
                        otherCodes.add(link);
                    }
                }
            } else {
                return "You input incorrect URL";
            }

            PrintResultInJsonFormat resultInJsonFormat = new PrintResultInJsonFormat();

            return resultInJsonFormat.createJson(url, code404, code50x, otherCodes);
        }

        public Iterator<URL> iterator() {
            ArrayList<URL> urls = returnLinksFromHTML(url);
            return urls.iterator();
        }
    }
}
