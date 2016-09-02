package com.epam.ali.gwt.sample.stockwatcher.server;

import com.epam.ali.gwt.sample.stockwatcher.client.StockPrice;
import com.epam.ali.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Random;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {
    private static final double MAX_PRICE = 100.0;
    private static final double MAX_PRICE_CHANGE = 0.02;

    @Override
    public StockPrice[] getPrices(String[] symbols) {
        Random random = new Random();

        StockPrice[] prices = new StockPrice[symbols.length];
        for (int i = 0; i < symbols.length; i++) {
            double price = random.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE * (random.nextDouble() * 2.0 - 1.0);
            prices[i] = new StockPrice(symbols[i], price, change);
        }
        return prices;
    }
}
