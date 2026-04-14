package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Quote;
import org.example.service.QodClientService;
import org.example.service.QuoteService;
import org.example.util.HtmlRenderer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet("/quotes")
public class QuotesPageServlet extends HttpServlet{

    private final QuoteService quoteService = AppContainer.QUOTE_SERVICE;
    private final QodClientService qodClientService = AppContainer.QOD_CLIENT_SERVICE;
    private final HtmlRenderer htmlRenderer = new HtmlRenderer();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Quote> savedQuotes = quoteService.getAllQuotes();
        Quote quoteOfTheDay = qodClientService.fetchQuoteOfTheDay();

        response.setStatus(HttpServletResponse.SC_OK);
        // saljem u html formatu
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // sa ovim upisujem HTML u HTTP odgovor
        PrintWriter out = response.getWriter();

        String html = htmlRenderer.renderQuotesPage(quoteOfTheDay, savedQuotes, request.getContextPath());

        out.println(html);

    }

}