package com.deadlinks;

import com.deadlinks.links.URLLinks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class Main2 {
    public static void main(String[] args) {
        String url = new String(args[0]);
        URLLinks urlLinks = null;

            urlLinks = new URLLinks(url);
            if (urlLinks.iterator().hasNext()) {
                for (Iterator<URL> it = urlLinks.iterator(); it.hasNext(); ) {
                    System.out.println(it.next().toString());
                }
            }
    }
}
