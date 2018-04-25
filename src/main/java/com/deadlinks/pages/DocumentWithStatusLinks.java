package com.deadlinks.pages;

import com.deadlinks.links.StatusLink;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DocumentWithStatusLinks implements DocumentWithLinks{

    private LinkedList<StatusLink> links;
    private Document document;

    private boolean validateLink(String url) {
        String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher m = pattern.matcher(url);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private void retrieveLinks() {
        Elements links = this.document.select("a[href]");
        this.links = new LinkedList<StatusLink>();
        System.out.println("Started links extraction.");
        for (Element link : links) {
            if (validateLink(link.attr("abs:href").toString())) {
                this.links.add(new StatusLink(link.attr("abs:href").toString()));
            }
        }
        System.out.println("Finished links extraction.");
    }

    @Override
    public Document document() {
        return this.document;
    }

    public LinkedList<StatusLink> linksWithStatus() {
        return this.links;
    }


    public DocumentWithStatusLinks(Document document) {
        this.document = document;
        this.retrieveLinks();
    }
}
