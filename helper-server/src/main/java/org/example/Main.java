package org.example;

import org.example.model.ParsedRequest;
import org.example.model.Quote;
import org.example.repository.HelperRepository;
import org.example.service.QuoteOfTheDayService;
import org.example.util.HttpRequestParser;
import org.example.util.HttpResponseWriter;
import org.example.util.InitialQuotesLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ULOGA KLASE:
 * - Ovo je ulazna tacka helper servera (interni servis za "quote of the day").
 *
 * DOPRINOS SISTEMU:
 * - Main server ne bira sam quote dana, nego ga trazi od helper servera.
 * - Ova klasa podize socket server i vraca JSON odgovor na GET /qod.
 *
 * KONKRETAN PRIMER:
 * - Korisnik otvori /quotes na glavnom serveru.
 * - Glavni server pozove helper: GET /qod.
 * - Ova klasa vrati npr. {"text":"Stay hungry, stay foolish.","author":"Steve Jobs"}.
 *
 * METODE UKRATKO:
 * - main(...)            -> pokrece server i loop za prihvat konekcija.
 * - handleClient(...)    -> cita zahtev, rutira putanju, vraca HTTP odgovor.
 * - writeResponse(...)   -> upisuje status, headere i body.
 * - loadInitialQuotes(...) -> ucitava pocetne citate iz resources fajla.
 * - parseLine(...)       -> jednu liniju iz fajla pretvara u Quote.
 */
public class Main {

    private static final int PORT = 8082;
    private static final String QOD_PATH = "/qod";
    private static final String INITIAL_QUOTES_RESOURCE = "initial-quotes.txt";
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        HelperRepository helperRepository = new HelperRepository();
        List<Quote> initialQuotes = InitialQuotesLoader.loadFromClassPath(INITIAL_QUOTES_RESOURCE);

        helperRepository.loadInitialQuotes(initialQuotes);
        QuoteOfTheDayService service = new QuoteOfTheDayService(helperRepository);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server slusa na "+ PORT + " portu\n");
            while(true){
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.submit(() -> {
                        handleClient(clientSocket, service);
                    });
                } catch (IOException e){
                    System.err.println("Greska pri prihvatanju konekcije: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Neuspesno pokretanje servera na portu " + PORT, e);
        } finally {
            executor.shutdown();
        }

    }

    private static void handleClient(Socket clientSocket, QuoteOfTheDayService service){

        try(
            Socket socket = clientSocket;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) ){

            HttpRequestParser requestParser = new HttpRequestParser(reader);
            HttpResponseWriter responseWriter = new HttpResponseWriter();

            ParsedRequest request;

            try{
                request = requestParser.parse();
            } catch (IllegalArgumentException e){
                responseWriter.writeTextResponse(writer, "400 Bad Request", "Bad request");
                return;
            }

            if(request == null)
                return;

            String method = request.getMethod();
            String path = request.getPath();

            System.out.println("Method: " + method);
            System.out.println("Path: " + path);

            if(!method.equals("GET")) {
                responseWriter.writeTextResponse(writer, "405 Method Not Allowed", "Method not allowed");
                return;
            }

            if(!path.equals(QOD_PATH)) {
                responseWriter.writeTextResponse(writer, "404 Not Found", "Not found");
                return;
            }

            Quote quote = service.getQod();
            String jsonBody = buildJsonResponse(quote);

            responseWriter.writeJsonResponse(writer, "200 OK", jsonBody);


        } catch (Exception e) {
            System.err.println("Greska pri obradi klijenta: " + e.getMessage());
        }
    }


    private static String buildJsonResponse(Quote quote){
        String text = escapeJson(quote.getText());
        String author = escapeJson(quote.getAuthor());
        return "{\"text\":\"" + text + "\",\"author\":\"" + author + "\"}";
    }


    private static String escapeJson(String value){
        if(value == null)
            return "";

        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }


}
