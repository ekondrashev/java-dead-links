package com.deadlinks.links;

import org.jsoup.Connection;

public class DeadLink implements Link {
    private String url;
    private Connection.Response response;
    private int statusCode;
    private boolean isDead = false;

    @Override
    public String url() {
        return this.url;
    }

//    @Override
//    public Connection.Response response() {
//        return this.response;
//    }

    //if not OK or nor FORBIDDEN
    private void checkIfDead() {
        if (this.statusCode != 200 || this.statusCode != 403) {
            this.isDead = true;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public DeadLink(StatusLink link) {
        this.url = link.url();
//        this.response = link.response();
        this.statusCode = link.statusCode();
    }
}
