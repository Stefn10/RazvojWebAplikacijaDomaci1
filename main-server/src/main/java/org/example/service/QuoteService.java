package org.example.service;

import org.example.model.Quote;
import org.example.repository.QuoteRepository;

import java.util.List;

public class QuoteService {

    private final QuoteRepository repository;

    public QuoteService(QuoteRepository repository){
        this.repository = repository;
    }

    public List<Quote> getAllQuotes(){
        return repository.findAll();
    }

    public void addQuote(String text, String author){
        if(text == null || text.trim().isEmpty()){
            throw new IllegalArgumentException("tekst citata je obavezan");
        }
        if(author == null || author.trim().isEmpty()){
            throw new IllegalArgumentException("autor citata je obavezan");
        }
        Quote quote = new Quote(text.trim(), author.trim());
        repository.save(quote);
    }



}
