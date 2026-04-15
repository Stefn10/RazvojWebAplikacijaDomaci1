package org.example.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

//utility klasa
public final class HttpResponseWriter {

    private HttpResponseWriter() {
    }

    public static void writeHtmlResponse(OutputStream out, String status, String html) throws IOException {
        writeResponse(
                out,
                status,
                "text/html; charset=UTF-8",
                html.getBytes(StandardCharsets.UTF_8),
                Map.of()
        );
    }

    public static void writeTextResponse(OutputStream out, String status, String text) throws IOException {
        writeResponse(
                out,
                status,
                "text/plain; charset=UTF-8",
                text.getBytes(StandardCharsets.UTF_8),
                Map.of()
        );
    }

    public static void writeRedirect303(OutputStream out, String location) throws IOException {
        String body = "Redirecting to " + location;
        writeResponse(
                out,
                "303 See Other",
                "text/plain; charset=UTF-8",
                body.getBytes(StandardCharsets.UTF_8),
                Map.of("Location", location)
        );
    }

    public static void writeResponse(
            OutputStream out,
            String status,
            String contentType,
            byte[] bodyBytes,
            Map<String, String> extraHeaders
    ) throws IOException {
        StringBuilder headers = new StringBuilder();

        headers.append("HTTP/1.1 ").append(status).append("\r\n");
        headers.append("Content-Type: ").append(contentType).append("\r\n");
        headers.append("Content-Length: ").append(bodyBytes.length).append("\r\n");
        headers.append("Connection: close\r\n");

        for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
            headers.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        headers.append("\r\n");

        out.write(headers.toString().getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
        out.flush();
    }
}
