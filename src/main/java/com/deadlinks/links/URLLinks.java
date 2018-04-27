package com.deadlinks.links;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class URLLinks implements Links {
    private ArrayList<URL> links;
    private URL parentURL;
    public String parentState;
    public String linksState;

    private void setParentURL(String url) {
        try {
            this.parentURL = new URL(url);
            parentState = "Valid URL";
        } catch (MalformedURLException e) {
            parentState = "Malformed URL.";
        }
    }

    private void retrieveLinks() {
        this.links = new ArrayList<>();
        try {
            Document document = Jsoup.connect(this.parentURL.toString()).get();
            Elements documentLinks = document.select("a[href]");
            System.out.println("Started links extraction.");
            for (Element link : documentLinks) {
                this.links.add(new URL(link.attr("abs:href")));
            }
            linksState = "Links were extracted succesfully.";
        } catch (MalformedURLException e) {
            linksState = "One of the retrieved links is malformed";
        } catch (IOException e) {
            linksState = "IOException while connecting";
        } catch (NullPointerException e) {
            linksState = "NullPointerException while reading parent URL.";
        }
    }

    public URL getParentURL() {
        return this.parentURL;
    }

    @Override
    public Iterator<URL> iterator() {
        return links.iterator();
    }

    public URLLinks(String url) {
        setParentURL(url);
        retrieveLinks();
    }
}
