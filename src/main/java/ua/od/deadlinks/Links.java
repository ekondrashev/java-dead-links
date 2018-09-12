package ua.od.deadlinks;


import java.net.URL;
import java.util.Iterator;
import java.util.List;


interface Links extends Iterable<URL> {

    class HTML implements Links {
        private String pageUrl;
        private List<URL> links;
        private String result;

        public HTML(String pageUrl) {
            this.pageUrl = pageUrl;
            this.links = new HtmlParser().getLinks(pageUrl);
        }


        @Override
        public Iterator<URL> iterator() {
            return links.iterator();
        }

        @Override
        public String toString() {
            return this.result = new UrlChecker(pageUrl).checkUrls(links);
        }
    }
}