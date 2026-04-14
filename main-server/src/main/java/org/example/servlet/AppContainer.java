package org.example.servlet;

import org.example.repository.QuoteRepository;
import org.example.service.QodClientService;
import org.example.service.QuoteService;
import org.example.util.InitialQuotesLoader;

public class AppContainer {
    public static final QuoteRepository QUOTE_REPOSITORY = new QuoteRepository();
    public static final QuoteService QUOTE_SERVICE = new QuoteService(QUOTE_REPOSITORY);
    public static final QodClientService QOD_CLIENT_SERVICE = new QodClientService();

    static {
        QUOTE_REPOSITORY.loadInitialQuotes(InitialQuotesLoader.loadFromClassPath("initial-quotes.txt"));
    }
}
