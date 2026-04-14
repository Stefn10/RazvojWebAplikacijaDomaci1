package org.example.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpResponseWriter {

    public void writeJsonResponse(BufferedWriter writer, String status, String body){
        writeResponse(writer, status, "application/json; charset=UTF-8", body);
    }

    public void writeTextResponse(BufferedWriter writer, String status, String body){
        writeResponse(writer, status, "text/plain; charset=UTF-8", body);
    }

    public void writeResponse(BufferedWriter writer, String status, String contentType, String body){
        try{
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

            writer.write("HTTP/1.1 " + status + "\r\n");
            writer.write("Content-Type: " + contentType + "\r\n");
            writer.write("Content-Length: " + bodyBytes.length + "\r\n");
            writer.write("Connection: close\r\n");
            writer.write("\r\n");
            writer.write(body);
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
