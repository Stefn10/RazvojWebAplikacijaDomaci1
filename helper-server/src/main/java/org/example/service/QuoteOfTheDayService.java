package org.example.service;

import org.example.model.Quote;
import org.example.repository.HelperRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class QuoteOfTheDayService {

    private final HelperRepository repository;

    public QuoteOfTheDayService(HelperRepository repository){
        this.repository = repository;
    }

    public Quote getQod(){
        List<Quote> quotes = repository.findAll();

        if(quotes.isEmpty())
            return new Quote("No quote available", "System");

        int index = ThreadLocalRandom.current().nextInt(quotes.size());
        return quotes.get(index);
    }


}
