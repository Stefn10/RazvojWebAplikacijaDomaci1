package org.example.util;

import javax.print.attribute.standard.RequestingUserName;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class HttpRequestParser {

    private static final int MAX_HEADER_BYTES = 32 * 1024;
    private final InputStream in;

    public HttpRequestParser(InputStream in){
        this.in = in;
    }

    public ParsedHttpRequest parse() throws IOException {
        byte[] headerBytes = readHeaderSection();
        if(headerBytes == null){
            return null;
        }

        String headerText = new String(headerBytes, StandardCharsets.ISO_8859_1);
        String[] lines = headerText.split("\r\n");

        if(lines.length == 0 || lines[0].isBlank()){
            return null;
        }

        String[] requestLineParts = lines[0].trim().split("\\s+");
        if(requestLineParts.length < 3){
            throw new IllegalArgumentException("Bad request line");
        }

        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String version = requestLineParts[2];

        Map<String, String> headers = new LinkedHashMap<>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty()) {
                continue;
            }
            int idx = line.indexOf(':');
            if (idx <= 0) {
                continue;
            }

            String name = line.substring(0, idx).trim().toLowerCase(Locale.ROOT);
            String value = line.substring(idx + 1).trim();
            headers.put(name, value);
        }

        int contentLength = parseContentLength(headers.get("content-length"));
        byte[] bodyBytes = readBody(contentLength);
        String body = bodyBytes.length == 0
                ? ""
                : new String(bodyBytes, resolveCharset(headers.get("content-type")));

        return new ParsedHttpRequest(method, path, version, headers, bodyBytes, body);

    }

    private byte[] readHeaderSection() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int matched = 0;
        int b;
        while ((b = in.read()) != -1) {
            buffer.write(b);

            switch (matched) {
                case 0 -> matched = (b == '\r') ? 1 : 0;
                case 1 -> matched = (b == '\n') ? 2 : ((b == '\r') ? 1 : 0);
                case 2 -> matched = (b == '\r') ? 3 : 0;
                case 3 -> {
                    if (b == '\n') {
                        byte[] all = buffer.toByteArray();
                        return Arrays.copyOf(all, all.length - 4);
                    }
                    matched = 0;
                }
                default -> matched = 0;
            }

            if (buffer.size() > MAX_HEADER_BYTES) {
                throw new IllegalArgumentException("Header section too large");
            }
        }
        if (buffer.size() == 0) {
            return null;
        }

        throw new IllegalArgumentException("Incomplete HTTP headers");
    }

    private int parseContentLength(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }

        try {
            int length = Integer.parseInt(value.trim());
            if (length < 0) {
                throw new NumberFormatException();
            }
            return length;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid Content-Length");
        }
    }

    private byte[] readBody(int length) throws IOException {
        if (length == 0) {
            return new byte[0];
        }

        byte[] body = in.readNBytes(length);
        if (body.length != length) {
            throw new IllegalArgumentException("Incomplete HTTP body");
        }
        return body;
    }

    private Charset resolveCharset(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return StandardCharsets.UTF_8;
        }

        String[] parts = contentType.split(";");
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.toLowerCase(Locale.ROOT).startsWith("charset=")) {
                String charsetName = trimmed.substring("charset=".length()).trim();
                if (charsetName.length() >= 2 && charsetName.startsWith("\"") && charsetName.endsWith("\"")) {
                    charsetName = charsetName.substring(1, charsetName.length() - 1);
                }
                try {
                    return Charset.forName(charsetName);
                } catch (Exception ignored) {
                    return StandardCharsets.UTF_8;
                }
            }
        }

        return StandardCharsets.UTF_8;
    }


}
