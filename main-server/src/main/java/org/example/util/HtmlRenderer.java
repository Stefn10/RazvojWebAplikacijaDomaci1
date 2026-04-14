package org.example.util;

import org.example.model.Quote;

import java.util.List;

// generisanje dinamickog html-a
public class HtmlRenderer {

    public String renderQuotesPage(Quote quoteOfTheDay, List<Quote> savedQuotes, String contextPath){
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Quotes</title>");
        html.append("""
                <style>
                    :root {
                        --bg: #f4f6fb;
                        --surface: #ffffff;
                        --text: #1f2937;
                        --muted: #5b6578;
                        --line: #e3e8f2;
                        --accent: #0f172a;
                    }

                    * {
                        box-sizing: border-box;
                    }

                    body {
                        margin: 0;
                        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                        color: var(--text);
                        background:
                            radial-gradient(circle at 10% 10%, #dbeafe 0%, transparent 35%),
                            radial-gradient(circle at 90% 90%, #fce7f3 0%, transparent 32%),
                            var(--bg);
                        min-height: 100vh;
                        display: flex;
                        justify-content: center;
                        padding: 32px 16px;
                    }

                    .container {
                        width: min(760px, 100%);
                        background: var(--surface);
                        border: 1px solid var(--line);
                        border-radius: 18px;
                        box-shadow: 0 18px 50px rgba(15, 23, 42, 0.08);
                        padding: 28px;
                    }

                    h1 {
                        margin: 0 0 20px;
                        font-size: clamp(1.8rem, 3vw, 2.3rem);
                    }

                    h2 {
                        margin: 24px 0 12px;
                        font-size: 1.45rem;
                    }

                    blockquote {
                        margin: 0;
                        padding: 14px 16px;
                        border-left: 4px solid var(--accent);
                        background: #f8fafc;
                        border-radius: 10px;
                        font-size: 1.08rem;
                        line-height: 1.5;
                    }

                    .author {
                        margin-top: 10px;
                        color: var(--muted);
                    }

                    ul {
                        margin: 0;
                        padding-left: 20px;
                        line-height: 1.6;
                    }

                    li + li {
                        margin-top: 4px;
                    }

                    .empty {
                        color: var(--muted);
                    }

                    form {
                        margin-top: 8px;
                        display: grid;
                        gap: 10px;
                    }

                    label {
                        font-weight: 600;
                    }

                    input {
                        width: 100%;
                        border: 1px solid #cbd5e1;
                        border-radius: 10px;
                        padding: 11px 12px;
                        font: inherit;
                    }

                    input:focus {
                        outline: 2px solid #cbd5e1;
                        outline-offset: 1px;
                    }

                    button {
                        width: fit-content;
                        border: none;
                        border-radius: 10px;
                        padding: 10px 16px;
                        background: var(--accent);
                        color: #fff;
                        font: inherit;
                        font-weight: 600;
                        cursor: pointer;
                    }

                    button:hover {
                        opacity: 0.92;
                    }
                </style>
                """);
        html.append("</head>");
        html.append("<body>");
        html.append("<main class=\"container\">");
        html.append("<h1>Quote Manager</h1>");

        html.append("<h2>Quote of the day</h2>");

        if (quoteOfTheDay != null) {
            html.append("<blockquote>")
                    .append(escapeHtml(quoteOfTheDay.getText()))
                    .append("</blockquote>");

            html.append("<p class=\"author\"><strong>")
                    .append(escapeHtml(quoteOfTheDay.getAuthor()))
                    .append("</strong></p>");
        } else {
            html.append("<p class=\"empty\">Quote of the day is currently unavailable.</p>");
        }

        html.append("<h2>Saved quotes</h2>");
        if (savedQuotes == null || savedQuotes.isEmpty()) {
            html.append("<p class=\"empty\">No saved quotes yet.</p>");
        } else {
            html.append("<ul>");

            for (Quote quote : savedQuotes) {
                html.append("<li>")
                        .append(escapeHtml(quote.getText()))
                        .append(" - ")
                        .append(escapeHtml(quote.getAuthor()))
                        .append("</li>");
            }

            html.append("</ul>");
        }

        html.append("<h2>Add quote</h2>");
        html.append("<form method=\"post\" action=\"").append(contextPath).append("/save-quote\">");

        html.append("<label for=\"text\">Text:</label>");
        html.append("<input type=\"text\" id=\"text\" name=\"text\" required>");

        html.append("<label for=\"author\">Author:</label>");
        html.append("<input type=\"text\" id=\"author\" name=\"author\" required>");

        html.append("<button type=\"submit\">Save Quote</button>");
        html.append("</form>");

        html.append("</main>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
