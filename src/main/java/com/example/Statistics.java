package com.example;

/**
 * Class to collect and store statistics for each data type.
 */
public class Statistics {
    private int count = 0;
    private long sum = 0;
    private double doubleSum = 0.0;
    private long minInteger = Long.MAX_VALUE;
    private long maxInteger = Long.MIN_VALUE;
    private double minDouble = Double.MAX_VALUE;
    private double maxDouble = -Double.MAX_VALUE;
    private int minStringLength = Integer.MAX_VALUE;
    private int maxStringLength = Integer.MIN_VALUE;

    public void addInteger(long value) {
        count++;
        sum += value;
        minInteger = Math.min(minInteger, value);
        maxInteger = Math.max(maxInteger, value);
    }

    public void addDouble(double value) {
        count++;
        doubleSum += value;
        minDouble = Math.min(minDouble, value);
        maxDouble = Math.max(maxDouble, value);
    }

    public void addString(String value) {
        count++;
        int length = value.length();
        minStringLength = Math.min(minStringLength, length);
        maxStringLength = Math.max(maxStringLength, length);
    }

    public int getCount() {
        return count;
    }

    public long getSum() {
        return sum;
    }

    public double getDoubleSum() {
        return doubleSum;
    }

    public long getMinInteger() {
        return minInteger;
    }

    public long getMaxInteger() {
        return maxInteger;
    }

    public double getMinDouble() {
        return minDouble;
    }

    public double getMaxDouble() {
        return maxDouble;
    }

    public int getMinStringLength() {
        return minStringLength;
    }

    public int getMaxStringLength() {
        return maxStringLength;
    }

    public double getAverageInteger() {
        return count > 0 ? (double) sum / count : 0;
    }

    public double getAverageDouble() {
        return count > 0 ? doubleSum / count : 0;
    }

    public boolean hasData() {
        return count > 0;
    }
}
