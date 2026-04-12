package com.mycompany.smartcampusapi.models;

public class SensorReading {

    private String readingId;
    private long timestamp;
    private double value;

    public SensorReading() {
    }

    public SensorReading(String readingId, long timestamp, double value) {
        this.readingId = readingId;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getReadingId() {
        return readingId;
    }

    public void setReadingId(String readingId) {
        this.readingId = readingId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}