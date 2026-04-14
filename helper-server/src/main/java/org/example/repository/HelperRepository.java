package org.example.repository;

import org.example.model.Quote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelperRepository {

    private List<Quote> quotes = Collections.synchronizedList(new ArrayList<>());

    public void loadInitialQuotes(List<Quote> initialQuotes){
        quotes.clear();
        quotes.addAll(initialQuotes);
    }

    // vracamo kopiju
    public List<Quote> findAll(){
        return new ArrayList<>(quotes);
    }
}
