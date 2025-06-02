package com.example.bigdata;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StockRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dateString;      // Data notowania w formacie String
    private Date date;              // Data jako obiekt Date
    private double open;            // Cena otwarcia
    private double high;            // Maksymalna cena
    private double low;             // Minimalna cena
    private double close;           // Cena zamknięcia (skorygowana o splity)
    private double adjClose;        // Cena zamknięcia (skorygowana o dywidendy i splity)
    private long volume;            // Liczba akcji
    private String stockSymbol;     // Symbol spółki

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public StockRecord(String dateString, String open, String high, String low,
                       String close, String adjClose, String volume, String stockSymbol) throws ParseException {
        this.dateString = dateString;
        this.date = sdf.parse(dateString); // Parsujemy datę
        this.open = Double.parseDouble(open);
        this.high = Double.parseDouble(high);
        this.low = Double.parseDouble(low);
        this.close = Double.parseDouble(close);
        this.adjClose = Double.parseDouble(adjClose);
        this.volume = Long.parseLong(volume);
        this.stockSymbol = stockSymbol;
    }

    public static StockRecord parseFromCSVLine(String csvLine) throws ParseException {
        // Date,Open,High,Low,Close,Adj Close,Volume,Stock
        String[] parts = csvLine.split(",");
        if (parts.length != 8) {
            throw new IllegalArgumentException("Niepoprawny format danych CSV: " + csvLine);
        }
        return new StockRecord(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
    }

    public long getTimestampInMillis() {
        return date.getTime();
    }

    // Gettery i Settery

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) throws ParseException {
        this.dateString = dateString;
        this.date = sdf.parse(dateString);
    }

    public Date getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    @Override
    public String toString() {
        return String.format("%s, %f, %f, %f, %f, %f, %d, %s",
                dateString, open, high, low, close, adjClose, volume, stockSymbol);
    }
}
