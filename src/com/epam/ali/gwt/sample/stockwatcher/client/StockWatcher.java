package com.epam.ali.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Iterator;

public class StockWatcher implements EntryPoint {

    private static final int REFRESH_INTERVAL = 3000;
    private static final String JSON_URL = GWT.getModuleBaseURL() + "json?symbols=";
    private StockWatcherConstants constants = GWT.create(StockWatcherConstants.class);
    private StockPriceServiceAsync stockPriceService = GWT.create(StockPriceService.class);
    private StockWatcherView stockWatcher = new StockWatcherView();


    public void onModuleLoad() {
        Window.setTitle(constants.stockWatcher());
        RootPanel.get("stockList").add(stockWatcher);

        Timer refreshTimer = new Timer() {
            @Override
            public void run() {
                refreshWatchList();
            }
        };
        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

    }

    private void refreshWatchList() {
        String url = JSON_URL;

        Iterator<String> iter = stockWatcher.getStocks().iterator();
        while (iter.hasNext()) {
            url += iter.next();
            if (iter.hasNext()) {
                url += "+";
            }
        }
        url = URL.encode(url);

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {


            Request request = builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        stockWatcher.updateTable(JsonUtils.<JsArray<StockData>>safeEval(response.getText()));
                    } else {
                        stockWatcher.setErrorMsg("Couldn't retrieve JSON (" + response.getStatusText() + ")");
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {

                }
            });
        } catch (RequestException e) {
            stockWatcher.setErrorMsg("Couldn't retrieve JSON");
        }
    }


}
