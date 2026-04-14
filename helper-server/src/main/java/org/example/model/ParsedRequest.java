package org.example.model;

public class ParsedRequest {

    private final String method;
    private final String path;

    public ParsedRequest(String method, String path){
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
