// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.models;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String roomId;
    private String roomName;
    private int capacity;
    private List<String> sensorIds = new ArrayList<>();

    public Room() {
    }

    public Room(String roomId, String roomName, int capacity) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public Room(String roomId, String roomName, int capacity, List<String> sensorIds) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.sensorIds = sensorIds;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = sensorIds;
    }
}