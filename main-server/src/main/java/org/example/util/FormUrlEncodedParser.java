package org.example.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

//utility klasa
public final class FormUrlEncodedParser {

    //nema pravljenja ovog objekata u drugim klasama
    private FormUrlEncodedParser() {
    }

    public static Map<String, String> parse(String body) {
        Map<String, String> values = new LinkedHashMap<>();

        if (body == null || body.isBlank()) {
            return values;
        }

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            if (pair.isEmpty()) {
                continue;
            }

            String[] keyValue = pair.split("=", 2);
            String key = decode(keyValue[0]);
            String value = keyValue.length > 1 ? decode(keyValue[1]) : "";

            values.put(key, value);
        }

        return values;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
