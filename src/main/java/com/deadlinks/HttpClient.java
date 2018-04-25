package com.deadlinks;

import com.deadlinks.links.StatusLink;
import com.deadlinks.pages.DocumentWithDeadLinks;
import com.deadlinks.pages.DocumentWithStatusLinks;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.IOException;

public final class HttpClient {

    public HttpClient(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        DocumentWithStatusLinks documentWithStatusLinks = new DocumentWithStatusLinks(document);
        DocumentWithDeadLinks documentWithDeadLinks = new DocumentWithDeadLinks(documentWithStatusLinks);
        documentWithDeadLinks.outputDeadLinks();
    }
}
