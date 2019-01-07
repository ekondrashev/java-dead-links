import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

interface Links extends Iterable<URL> {

    class HTML implements Links {

        private ArrayList<String> err404;
        private ArrayList<String> err50x;
        private String urlForCheck;
        private ArrayList<URL> urls;
        private int dead = 0;
        private int total = 0;

        public HTML(String urlForCheck) {
            urlForCheck = urlForCheck.trim();
            this.urlForCheck = urlForCheck.trim();
            this.err50x = new ArrayList<>();
            this.err404 = new ArrayList<>();
            try {
                findDeadLinks();
            } catch (IOException e) {
                System.err.print(e.getMessage());
            }
        }


        @Override
        public Iterator<URL> iterator() {
            this.urls = new ArrayList<>();
            try {
                String protocol = urlForCheck.split("://")[0];
                Document document = Jsoup.connect(urlForCheck).get();
                Elements links = document.select("a[href]");

                for (Element link : links) {
                    String href = link.attr("href");
                    if (!(href.contains("tel:")) && !(href.contains("javascript:"))) {
                        if (href.matches("^//.*?")) {
                            href = protocol + ":" + href;
                        } else if (!href.matches("^http.*?")) {
                            if (!href.matches("^/.*?")) {
                                href = urlForCheck + "/" + href;
                            } else {
                                href = urlForCheck + href;
                            }
                        }
                        this.urls.add(new URL(href));
                    }
                }
            } catch (Exception e) {
                System.err.println("Illegal URL");
            }
            return this.urls.iterator();
        }

        private void findDeadLinks() throws IOException {
            for (URL url : this) {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int code = -1;
                code = connection.getResponseCode();
                if (code == 404) {
                    this.err404.add(url.toString());
                    this.dead++;
                }
                if (code >= 500 && code < 600) {
                    this.err50x.add(url.toString());
                    this.dead++;
                } else {
                    this.total++;
                }
            }
        }

        @Override
        public String toString() {
            return "{\n" +
                "\t\"url\":\" " + urlForCheck + "\",\n" +
                "\t\"404\": {\n" +
                "\t\t\"size\": " + err404.size() + ",\n" +
                "\t\t\"urls\": " + err404 +
                "\t\n},\n" +
                "\t\"50x\": {\n" +
                "\t\t\"size\": " + err50x.size() + ",\n" +
                "\t\t\"urls\": " + err50x +
                "\t\n},\n" +
                "\t\"dead\": " + dead + ",\n" +
                "\t\"total\": " + total +
                "\n}";
        }
    }
}
