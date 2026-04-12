// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.models;

public class Sensor {

    private String sensorId;
    private String sensorType;
    private String sensorStatus;
    private double currentValue;
    private String roomId;

    public Sensor() {
    }

    public Sensor(String sensorId, String sensorType, String sensorStatus, double currentValue, String roomId) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.sensorStatus = sensorStatus;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}