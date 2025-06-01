package com.example.stocketl.model;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class StockRecord implements Serializable {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjClose;
    private long volume;
    private String stock;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static StockRecord parseFromCsvLine(String csvLine) {
        // CSV: Date,Open,High,Low,Close,Adj Close,Volume,Stock
        String[] parts = csvLine.split(",");
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid CSV line: " + csvLine);
        }
        StockRecord record = new StockRecord();
        record.date = LocalDate.parse(parts[0], DATE_FORMAT);
        record.open = Double.parseDouble(parts[1]);
        record.high = Double.parseDouble(parts[2]);
        record.low = Double.parseDouble(parts[3]);
        record.close = Double.parseDouble(parts[4]);
        record.adjClose = Double.parseDouble(parts[5]);
        record.volume = Long.parseLong(parts[6]);
        record.stock = parts[7];
        return record;
    }
    @Override
    public String toString() {
        return String.format("StockRecord{date='%s', stock='%s', close=%.2f, high=%.2f, low=%.2f, volume=%d}",
                date, stock, close, high, low, volume);
    }
}
