package com.example.stocketl.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class SymbolMetaRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter private String nasdaqTraded;
    @Getter private String symbol;
    @Getter private String securityName;
    @Getter private String listingExchange;
    @Getter private String marketCategory;
    @Getter private String etf;
    @Getter private int roundLotSize;
    @Getter private String testIssue;
    @Getter private String financialStatus;
    @Getter private String nextShares;


    public static SymbolMetaRecord parseFromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid CSV line: " + csvLine);
        }

        SymbolMetaRecord record = new SymbolMetaRecord();
        record.nasdaqTraded = parts[0];
        record.symbol = parts[1];
        record.securityName = parts[2];
        record.listingExchange = parts[3];
        record.marketCategory = parts[4];
        record.etf = parts[5];
        record.roundLotSize = Integer.parseInt(parts[6]);
        record.testIssue = parts[7];
        record.financialStatus = parts[8];
        record.nextShares = parts[9];

        return record;
    }

    @Override
    public String toString() {
        return String.format("SymbolMetaRecord{symbol='%s', name='%s', marketCategory='%s'}",
                symbol, securityName, marketCategory);
    }
}
