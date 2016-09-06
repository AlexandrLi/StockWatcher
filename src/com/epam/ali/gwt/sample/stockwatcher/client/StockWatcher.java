package com.epam.ali.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class StockWatcher implements EntryPoint {

    private static final int REFRESH_INTERVAL = 3000;
    private StockWatcherConstants constants = GWT.create(StockWatcherConstants.class);
    private StockPriceServiceAsync stockPriceService = GWT.create(StockPriceService.class);
    private StockWatcherUI stockWatcher = new StockWatcherUI();


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
        if (stockPriceService == null) {
            stockPriceService = GWT.create(StockPriceService.class);
        }
        AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {
            @Override
            public void onFailure(Throwable caught) {
                String details = caught.getMessage();
                if (caught instanceof DelistedException) {
                    details = "Company '" + ((DelistedException) caught).getSymbol() + "' was delisted";
                }
                stockWatcher.setErrorMsg(details);
            }

            @Override
            public void onSuccess(StockPrice[] result) {
                stockWatcher.updateTable(result);
            }
        };
        stockPriceService.getPrices(stockWatcher.getStocks().toArray(new String[0]), callback);
    }


}
