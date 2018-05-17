package com.deadlinks;

public enum UrlStatus {

    HTTP_OK (200),
    NO_CONTENT (204),
    MOVED_PERMANENTLY (301),
    NOT_MODIFIED(304),
    USE_PROXY(305),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private int statusCode;

    UrlStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
