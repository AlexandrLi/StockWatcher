package com.epam.ali.gwt.sample.stockwatcher.client.view;

import com.epam.ali.gwt.sample.stockwatcher.client.i18n.StockWatcherConstants;
import com.epam.ali.gwt.sample.stockwatcher.client.i18n.StockWatcherMessages;
import com.epam.ali.gwt.sample.stockwatcher.client.model.StockData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockWatcherView extends Composite {
    private static StockWatcherViewUiBinder ourUiBinder = GWT.create(StockWatcherViewUiBinder.class);
    @UiField
    VerticalPanel mainPanel;
    @UiField
    HorizontalPanel addPanel;
    @UiField
    Label errorMsgLabel;
    @UiField
    Label lastUpdateLabel;
    @UiField
    Label title;
    @UiField()
    FlexTable stocksFlexTable;
    @UiField
    TextBox newSymbolTextBox;
    @UiField
    Button addStockButton;
    private List<String> stocks = new ArrayList<String>();
    private StockWatcherConstants constants = GWT.create(StockWatcherConstants.class);
    private StockWatcherMessages messages = GWT.create(StockWatcherMessages.class);

    public StockWatcherView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        title.setText(constants.stockWatcher());
        initFlexTable();
        initAddStockButton();
    }

    public List<String> getStocks() {
        return stocks;
    }

    public void setStocks(List<String> stocks) {
        this.stocks = stocks;
    }

    public void setErrorMsg(String message) {
        errorMsgLabel.setText(message);
        errorMsgLabel.setVisible(true);
    }

    private void initFlexTable() {
        stocksFlexTable.setText(0, 0, constants.symbol());
        stocksFlexTable.setText(0, 1, constants.price());
        stocksFlexTable.setText(0, 2, constants.change());
        stocksFlexTable.setText(0, 3, constants.remove());
        stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
        stocksFlexTable.addStyleName("watchList");
        stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
    }

    private void initAddStockButton() {
        addStockButton.setText(constants.add());
    }

    @UiHandler("addStockButton")
    void handleClick(ClickEvent event) {
        addStock();
    }

    @UiHandler("newSymbolTextBox")
    void handleKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            addStock();
        }
    }

    private void addStock() {
        final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
        newSymbolTextBox.setFocus(true);

        if (!symbol.matches("[0-9A-Z\\\\.]{1,10}$")) {
            Window.alert(messages.invalidSymbol(symbol));
            newSymbolTextBox.selectAll();
            return;
        }
        if (stocks.contains(symbol)) {
            Window.alert(messages.alreadyExists(symbol));
            newSymbolTextBox.selectAll();
            return;
        }
        int row = stocksFlexTable.getRowCount();
        stocks.add(symbol);
        stocksFlexTable.setText(row, 0, symbol);
        stocksFlexTable.setWidget(row, 2, new Label());
        stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");
        newSymbolTextBox.setText("");

        Button removeStockButton = new Button("x", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int removedIndex = stocks.indexOf(symbol);
                stocks.remove(removedIndex);
                stocksFlexTable.removeRow(removedIndex + 1);
            }
        });
        removeStockButton.addStyleDependentName("remove");
        stocksFlexTable.setWidget(row, 3, removeStockButton);
    }

    public void updateTable(JsArray<StockData> prices) {
        for (int i = 0; i < prices.length(); i++) {
            updateTable(prices.get(i));
        }
        setLastUpdateDate(new Date());
    }

    private void setLastUpdateDate(Date date) {
        lastUpdateLabel.setText(messages.lastUpdate(date));
        errorMsgLabel.setVisible(false);
    }

    private void updateTable(StockData price) {
        if (!stocks.contains(price.getSymbol())) {
            return;
        }
        int row = stocks.indexOf(price.getSymbol()) + 1;
        String priceText = NumberFormat.getFormat("#,##0.00").format(price.getPrice());
        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
        String changeText = changeFormat.format(price.getChange());
        String changePercentText = changeFormat.format(price.getChangePercent());

        stocksFlexTable.setText(row, 1, priceText);
        Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);
        changeWidget.setText(changeText + " (" + changePercentText + "%)");
        String changeStyleName = "noChange";
        if (price.getChangePercent() < -0.1) {
            changeStyleName = "negativeChange";
        } else if (price.getChangePercent() > 0.1) {
            changeStyleName = "positiveChange";
        }
        changeWidget.setStyleName(changeStyleName);
    }

    interface StockWatcherViewUiBinder extends UiBinder<HTMLPanel, StockWatcherView> {
    }
}