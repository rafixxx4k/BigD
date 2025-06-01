package com.example.stocketl.model;

import lombok.Getter;

public class AggregationResult {
    private double sumClose = 0;
    private long count = 0;
    private double minLow = Double.MAX_VALUE;
    private double maxHigh = Double.MIN_VALUE;
    @Getter private long sumVolume = 0;

    public AggregationResult add(EnrichedStockRecord record) {
        double close = record.getStock().getClose();
        double low = record.getStock().getLow();
        double high = record.getStock().getHigh();
        long volume = record.getStock().getVolume();

        sumClose += close;
        count++;
        if (low < minLow) minLow = low;
        if (high > maxHigh) maxHigh = high;
        sumVolume += volume;

        return this;
    }

    public double getAvgClose() {
        return count == 0 ? 0 : sumClose / count;
    }

    public double getMinLow() {
        return count == 0 ? 0 : minLow;
    }

    public double getMaxHigh() {
        return count == 0 ? 0 : maxHigh;
    }

    @Override
    public String toString() {
        return "AggregationResult{" +
                "avgClose=" + getAvgClose() +
                ", minLow=" + getMinLow() +
                ", maxHigh=" + getMaxHigh() +
                ", sumVolume=" + getSumVolume() +
                '}';
    }
}
