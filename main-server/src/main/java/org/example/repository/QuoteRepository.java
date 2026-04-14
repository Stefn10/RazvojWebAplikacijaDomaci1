package org.example.repository;

import org.example.model.Quote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuoteRepository {

    private final List<Quote> quotes = Collections.synchronizedList(new ArrayList<>());

    public void save(Quote quote){
        quotes.add(quote);
    }

    //vracamo kopiju liste, umesto interne strukture
    public List<Quote> findAll(){
        return new ArrayList<>(quotes);
    }

    public void loadInitialQuotes(List<Quote> initialQuotes){
        quotes.clear();
        quotes.addAll(initialQuotes);
    }
}
