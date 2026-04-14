package org.example.service;

import org.example.model.Quote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ULOGA KLASE:
 * - Klijent ka helper serveru: salje HTTP GET /qod preko Socket-a.
 *
 * DOPRINOS SISTEMU:
 * - Spaja glavni i pomocni servis bez gotovog HTTP klijenta (sto trazi zadatak).
 *
 * KONKRETAN PRIMER:
 * - QuotesPageServlet treba da prikaze "quote dana".
 * - Pozove fetchQuoteOfTheDay(), ova klasa vrati Quote i servlet ga ubaci u HTML.
 * - Ako helper padne, vraca fallback quote da stranica i dalje radi.
 *
 * METODE UKRATKO:
 * - fetchQuoteOfTheDay() -> kompletan tok: connect, request, response, parse.
 * - readHeaders(...)     -> cita HTTP headere iz odgovora.
 * - readBody(...)        -> cita telo odgovora (po Content-Length ako postoji).
 * - parseQuoteFromJson(...) -> izvlaci text i author iz JSON stringa.
 */

public class QodClientService { // komunicira sa pomocnim serverom

    private static final Pattern FIELD_PATTERN_TEMPLATE =
            Pattern.compile("\"%s\"\\s*:\\s*\"((?:\\\\.|[^\\\\\"])*)\"");
    // s - uzima neki string na pocetku kao author/text ...
    // dozvoli razmake oko :
    // da vrednost bude pod navodnicima
    // uzme escape znakove ili bilo koje druge
    //

    private final String host;
    private final int port;
    private final String path;

    // jedan defaultni
    public QodClientService() {
        this("localhost", 8082, "/qod");
    }
    // TODO da li su morala dva konstruktora i zasto smo ih napravili??
    // jedan custom konstruktor
    public QodClientService(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public Quote fetchQuoteOfTheDay() {
        // oni ce se sami zatvoriti ako su u zagradama, jer implementiraju AutoCloseable
        try (Socket socket = new Socket(host, port);
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream() ,StandardCharsets.UTF_8));
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
        ) {

            writer.write("GET " + path + " HTTP/1.1\r\n");
            writer.write("Host: " + host + ":" + port + "\r\n");
            writer.write("Connection: close\r\n");
            writer.write("\r\n"); // gotovi su headeri
            writer.flush();

            String statusLine = reader.readLine();
            if (statusLine == null || !statusLine.contains("200")) {
                return fallbackQuote();
            }

            Map<String, String> headers = readHeaders(reader);
            String body = readBody(reader, headers.get("content-length"));

            return parseQuoteFromJson(body);

        } catch (Exception e) {
            return fallbackQuote();
        }
    }

    private Map<String, String> readHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int idx = line.indexOf(':');
            if (idx > 0) {
                String name = line.substring(0, idx).trim().toLowerCase();
                String value = line.substring(idx + 1).trim();
                headers.put(name, value);
            }
        }
        return headers;
    }

    private String readBody(BufferedReader reader, String contentLengthHeader) throws IOException {
        if (contentLengthHeader != null) {
            int length = Integer.parseInt(contentLengthHeader);
            char[] buffer = new char[length];
            int totalRead = 0;
            while (totalRead < length) { // podaci se mozda ne procitaju odjednom
                int read = reader.read(buffer, totalRead, length - totalRead); // cita od dela gde je stao
                if (read == -1) {// ako je procitao sve
                    break;
                }
                totalRead += read; // saberi sa onim sto je u ovom pozivu procitao
            }
            return new String(buffer, 0, totalRead);
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private Quote parseQuoteFromJson(String json) {
        String text = extractField(json, "text");
        String author = extractField(json, "author");

        if (text == null || text.isBlank() || author == null || author.isBlank()) {
            return fallbackQuote();
        }

        return new Quote(unescapeJson(text), unescapeJson(author));
    }

    private String extractField(String json, String fieldName) {
        // pravilo pretrage
        Pattern pattern = Pattern.compile(String.format(FIELD_PATTERN_TEMPLATE.pattern(), Pattern.quote(fieldName)));
        // uzima deo posle ':'
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String unescapeJson(String value) {
        return value
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }

    private Quote fallbackQuote() {
        return new Quote("Quote of the day is currently unavailable.", "System");
    }
}
