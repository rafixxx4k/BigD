package com.example.bigdata;

import java.io.Serializable;

public class StockSymbolRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private StockRecord stockRecord;
    private SymbolRecord symbolRecord;

    public StockSymbolRecord(StockRecord stockRecord, SymbolRecord symbolRecord) {
        this.stockRecord = stockRecord;
        this.symbolRecord = symbolRecord;
    }

    public StockRecord getStockRecord() {
        return stockRecord;
    }

    public SymbolRecord getSymbolRecord() {
        return symbolRecord;
    }

    @Override
    public String toString() {
        return "StockSymbolRecord{" +
                "stockRecord=" + stockRecord +
                ", symbolRecord=" + symbolRecord +
                '}';
    }
}
