package org.example.util;

import org.example.model.ParsedRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestParser {

    private final BufferedReader reader;

    public HttpRequestParser(BufferedReader reader){
        this.reader = reader;
    }

    public ParsedRequest parse(){
        try{
            String requestLine = reader.readLine();

            if(requestLine == null || requestLine.isBlank())
                return null;

            String[] parts = requestLine.split(" ");

            if(parts.length < 3)
                throw new IllegalArgumentException("Bad request line");

            String method = parts[0];
            String path = parts[1];

            return new ParsedRequest(method, path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
