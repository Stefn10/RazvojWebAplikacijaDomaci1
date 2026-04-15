package org.example.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParsedHttpRequest {

    private final String method;
    private final String path;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final byte[] bodyBytes;
    private final String body;

    public ParsedHttpRequest(String method, String path, String httpVersion, Map<String, String> headers, byte[] bodyBytes, String body){
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        // kopija prosledjene mape, zapamcena po redosledu kako je pisano u nju i ona se ne menja vise
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(headers));
        this.bodyBytes = bodyBytes;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    public String getBody() {
        return body;
    }
}
