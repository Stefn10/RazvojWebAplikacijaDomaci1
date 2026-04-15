//package org.example;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class Main {
//
//    private static final int PORT = 8080;
//    private static final String QOD_PATH = "/qod";
//    private static final Random RANDOM = new Random();
//
//    public static void main(String[] args) throws IOException {
//        List<Quote> quotes = loadInitialQuotes("initial-quotes.txt");
//        if (quotes.isEmpty()) {
//            quotes.add(new Quote("Stay hungry, stay foolish.", "Steve Jobs"));
//            quotes.add(new Quote("Simplicity is the ultimate sophistication.", "Leonardo da Vinci"));
//        }
//
//        ExecutorService pool = Executors.newCachedThreadPool();
//
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Helper server listening on port " + PORT);
//
//            while (true) {
//                Socket clientSocket = serverSocket.accept();
//                pool.submit(() -> handleClient(clientSocket, quotes));
//            }
//        }
//    }
//
//    private static void handleClient(Socket socket, List<Quote> quotes) {
//        try (socket;
//             BufferedReader reader = new BufferedReader(
//                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
//             OutputStream out = socket.getOutputStream()) {
//
//            String requestLine = reader.readLine();
//            if (requestLine == null || requestLine.isBlank()) {
//                return;
//            }
//
//            String headerLine;
//            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
//                // preskacemo header-e za ovu minimalnu verziju
//            }
//
//            String[] parts = requestLine.split(" ");
//            String method = parts.length > 0 ? parts[0] : "";
//            String path = parts.length > 1 ? parts[1] : "";
//
//            if ("GET".equalsIgnoreCase(method) && QOD_PATH.equals(path)) {
//                Quote quote = quotes.get(RANDOM.nextInt(quotes.size()));
//                String json = "{\"text\":\"" + escapeJson(quote.text()) + "\",\"author\":\"" + escapeJson(quote.author()) + "\"}";
//                writeResponse(out, "200 OK", "application/json; charset=UTF-8", json);
//            } else {
//                writeResponse(out, "404 Not Found", "text/plain; charset=UTF-8", "Not found");
//            }
//
//        } catch (Exception e) {
//            // minimal helper: greške po konekciji ne ruše server
//        }
//    }
//
//    private static void writeResponse(OutputStream out, String status, String contentType, String body) throws IOException {
//        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
//        String headers =
//                "HTTP/1.1 " + status + "\r\n" +
//                        "Content-Type: " + contentType + "\r\n" +
//                        "Content-Length: " + bodyBytes.length + "\r\n" +
//                        "Connection: close\r\n" +
//                        "\r\n";
//
//        out.write(headers.getBytes(StandardCharsets.UTF_8));
//        out.write(bodyBytes);
//        out.flush();
//    }
//
//    private static List<Quote> loadInitialQuotes(String resourceName) throws IOException {
//        List<Quote> quotes = new ArrayList<>();
//
//        InputStream is = Main.class.getClassLoader().getResourceAsStream(resourceName);
//        if (is == null) {
//            return quotes;
//        }
//
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                Quote quote = parseLine(line);
//                if (quote != null) {
//                    quotes.add(quote);
//                }
//            }
//        }
//
//        return quotes;
//    }
//
//    private static Quote parseLine(String line) {
//        if (line == null || line.isBlank()) {
//            return null;
//        }
//
//        int sep = line.lastIndexOf(" - ");
//        if (sep < 0) {
//            return null;
//        }
//
//        String text = line.substring(0, sep).trim();
//        String author = line.substring(sep + 3).trim();
//
//        text = stripOuterQuotes(text);
//        author = stripOuterQuotes(author);
//
//        if (author.endsWith(".")) {
//            author = author.substring(0, author.length() - 1).trim();
//        }
//
//        if (text.isBlank() || author.isBlank()) {
//            return null;
//        }
//
//        return new Quote(text, author);
//    }
//
//    private static String stripOuterQuotes(String s) {
//        if (s == null || s.length() < 2) {
//            return s;
//        }
//
//        char first = s.charAt(0);
//        char last = s.charAt(s.length() - 1);
//
//        boolean classic = first == '"' && last == '"';
//
//        if (classic) {
//            return s.substring(1, s.length() - 1).trim();
//        }
//        return s;
//    }
//
//    private static String escapeJson(String value) {
//        return value
//                .replace("\\", "\\\\")
//                .replace("\"", "\\\"")
//                .replace("\n", "\\n")
//                .replace("\r", "\\r")
//                .replace("\t", "\\t");
//    }
//
//    private record Quote(String text, String author) {
//    }
//}
