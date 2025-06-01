package com.example.stocketl.model;

public class EnrichedStockRecord {
    private StockRecord stock;
    private String securityName;

    public EnrichedStockRecord(StockRecord stock, String securityName) {
        this.stock = stock;
        this.securityName = securityName;
    }

    public StockRecord getStock() {
        return stock;
    }

    public String getSecurityName() {
        return securityName;
    }

    public String getMonthSymbolKey() {
        // np. 2024-05_SYMBOL_SecurityName
        String month = stock.getDate().getYear() + "-" + String.format("%02d", stock.getDate().getMonthValue());
        return month + "_" + stock.getStock() + "_" + securityName.replaceAll("\\s+", "_");
    }
}
