package com.epam.ali.gwt.sample.stockwatcher.server;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@SuppressWarnings("NonJREEmulationClassesInClientCode")
public class JsonStockData extends HttpServlet {
    private static final double MAX_PRICE = 100.0;
    private static final double MAX_PRICE_CHANGE = 0.02;

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

        Random random = new Random();

        PrintWriter out = res.getWriter();

        String[] stockSymbols = req.getParameter("symbols").split(" ");
        boolean firstSymbol = true;
        out.println('[');
        for (String stockSymbol : stockSymbols) {
            double price = random.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE * (random.nextDouble() * 2.0 - 1.0);
            if (firstSymbol) {
                firstSymbol = false;
            } else {
                out.println(" ,");
            }
            out.println("  {");
            out.print("    \"symbol\": \"");
            out.print(stockSymbol);
            out.println("\",");
            out.print("    \"price\": ");
            out.print(price);
            out.println(',');
            out.print("    \"change\": ");
            out.println(change);
            out.println("  }");
        }
        out.println(']');
        out.flush();
    }
}

