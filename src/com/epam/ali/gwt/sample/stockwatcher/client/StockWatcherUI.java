package com.epam.ali.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StockWatcherUI extends Composite {
    private static StockWatcherUIUiBinder ourUiBinder = GWT.create(StockWatcherUIUiBinder.class);
    @UiField
    VerticalPanel mainPanel;

    public StockWatcherUI() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    interface StockWatcherUIUiBinder extends UiBinder<HTMLPanel, StockWatcherUI> {
    }
}