package com.epam.ali.gwt.sample.stockwatcher.client;

import java.io.Serializable;

public class DelistedException extends Exception implements Serializable {
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public DelistedException() {
    }

    public DelistedException(String symbol) {
        this.symbol = symbol;
    }
}
