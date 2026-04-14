package org.example.util;

import org.example.model.Quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InitialQuotesLoader {

    private InitialQuotesLoader(){
    }

    public static List<Quote> loadFromClassPath(String name){
        List<Quote> quotes = new ArrayList<>();

        InputStream is = InitialQuotesLoader.class.getClassLoader().getResourceAsStream(name);
        if(is == null){
            return quotes;
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                Quote quote = parseLine(line);
                if(quote != null){
                    quotes.add(quote);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Greska pri ucitavanju inicijalnih citata iz mog fajla: " + name, e);
        }
        return quotes;
    }

    private static Quote parseLine(String line){
        if(line == null || line.isBlank()) {
            return null;
        }

        int sep = line.lastIndexOf(" - ");
        if(sep < 0)
            return null;

        String text = line.substring(0, sep). trim();
        // zasto plus 3??
        String author = line.substring(sep + 3). trim();

        text = stripOuterQuotes(text);
        author = stripOuterQuotes(author);

        if(author.endsWith("."))
            author = author.substring(0, author.length() - 1).trim();

        if(text.isBlank() || author.isBlank())
            return null;

        return new Quote(text, author);


    }

    private static String stripOuterQuotes(String s){
        if( s == null || s.length() < 2)
            return s;

        char first = s.charAt(0);
        char last = s.charAt(s.length() - 1);

        boolean classic = first == '"' && last == '"';
        boolean fancy = first == '“' && last == '”';

        if(classic || fancy)
            return s.substring(1, s.length() - 1).trim();

        return s;
    }


}
