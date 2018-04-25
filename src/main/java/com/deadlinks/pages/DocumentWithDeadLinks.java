package com.deadlinks.pages;

import com.deadlinks.links.DeadLink;
import com.deadlinks.links.StatusLink;
import org.jsoup.nodes.Document;

import java.util.LinkedList;

public class DocumentWithDeadLinks implements DocumentWithLinks {
    private LinkedList<StatusLink> links;
    private LinkedList<DeadLink> deadLinks;
    private Document document;

    private void retrieveDeadLinks() {
        this.deadLinks = new LinkedList<DeadLink>();
        for (StatusLink link: this.links) {
            DeadLink deadLink = new DeadLink(link);
            if (deadLink.isDead()) {
                deadLinks.add(deadLink);
            }
        }
    }

    @Override
    public Document document() {
        return this.document;
    }

    public void outputDeadLinks() {
        System.out.println(Integer.toString(deadLinks.size()) + " dead links were found.");
        for (DeadLink deadLink : this.deadLinks) {
            System.out.println(deadLink.url());
        }
    }

    public DocumentWithDeadLinks(DocumentWithStatusLinks documentWithStatusLinks) {
        this.links = documentWithStatusLinks.linksWithStatus();
        this.document = documentWithStatusLinks.document();
        retrieveDeadLinks();
    }
}
