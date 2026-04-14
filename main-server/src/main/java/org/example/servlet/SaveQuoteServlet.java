package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.QuoteService;

import java.io.IOException;

@WebServlet("/save-quote")
public class SaveQuoteServlet extends HttpServlet {

    private final QuoteService quoteService = AppContainer.QUOTE_SERVICE;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String text = request.getParameter("text");
        String author = request.getParameter("author");

        try {
            quoteService.addQuote(text, author);
            response.sendRedirect(request.getContextPath() + "/quotes");
        } catch (IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(ex.getMessage());
        }
    }
}
