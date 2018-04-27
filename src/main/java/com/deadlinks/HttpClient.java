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

// static method make some working test
// interface + instructions
//class Link() - remove just leave class URL from java.net
//make simpler
//Links -> iterable implementation that returns links from html foreach