package com.example.bigdata;

import java.io.Serializable;

public class SymbolRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nasdaqTraded;
    private String symbol;
    private String securityName;
    private String listingExchange;
    private String marketCategory;
    private String etf;
    private String roundLotSize;
    private String testIssue;
    private String financialStatus;
    private String cqsSymbol;
    private String nasdaqSymbol;
    private String nextShares;

    public SymbolRecord(String nasdaqTraded, String symbol, String securityName,
                        String listingExchange, String marketCategory, String etf,
                        String roundLotSize, String testIssue, String financialStatus,
                        String cqsSymbol, String nasdaqSymbol, String nextShares) {
        this.nasdaqTraded = nasdaqTraded;
        this.symbol = symbol;
        this.securityName = securityName;
        this.listingExchange = listingExchange;
        this.marketCategory = marketCategory;
        this.etf = etf;
        this.roundLotSize = roundLotSize;
        this.testIssue = testIssue;
        this.financialStatus = financialStatus;
        this.cqsSymbol = cqsSymbol;
        this.nasdaqSymbol = nasdaqSymbol;
        this.nextShares = nextShares;
    }

    public static SymbolRecord parseFromCSVLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 12) {
            throw new IllegalArgumentException("Niepoprawny format linii CSV: " + csvLine);
        }

        return new SymbolRecord(
                parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim(),
                parts[8].trim(), parts[9].trim(), parts[10].trim(), parts[11].trim()
        );
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSecurityName() {
        return securityName;
    }

    public String getMarketCategory() {
        return marketCategory;
    }

    public String getFinancialStatus() {
        return financialStatus;
    }

    @Override
    public String toString() {
        return String.format("Symbol=%s, Name=%s, MarketCategory=%s, FinancialStatus=%s",
                symbol, securityName, marketCategory, financialStatus);
    }
}
