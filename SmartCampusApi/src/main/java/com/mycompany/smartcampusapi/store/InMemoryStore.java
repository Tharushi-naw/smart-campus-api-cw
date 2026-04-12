// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.store;

import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.models.SensorReading;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryStore {

    // Stores all rooms using roomId as the key
    public static final Map<String, Room> roomStore = new LinkedHashMap<>();

    // Stores all sensors using sensorId as the key
    public static final Map<String, Sensor> sensorStore = new LinkedHashMap<>();

    // Stores reading history for each sensor
    public static final Map<String, List<SensorReading>> readingHistoryStore = new LinkedHashMap<>();

    static {
        // Sample room data added when the application starts
        Room libraryRoom = new Room("LIB-301", "Library Quiet Study", 40);
        Room labRoom = new Room("ENG-201", "Engineering Lab", 30);

        roomStore.put(libraryRoom.getRoomId(), libraryRoom);
        roomStore.put(labRoom.getRoomId(), labRoom);

        // Sample sensor data
        Sensor temperatureSensor = new Sensor("TEMP-001", "Temperature", "ACTIVE", 24.5, "LIB-301");
        Sensor co2Sensor = new Sensor("CO2-001", "CO2", "MAINTENANCE", 420.0, "ENG-201");

        sensorStore.put(temperatureSensor.getSensorId(), temperatureSensor);
        sensorStore.put(co2Sensor.getSensorId(), co2Sensor);

        // Link sensors to their related rooms
        roomStore.get("LIB-301").getSensorIds().add("TEMP-001");
        roomStore.get("ENG-201").getSensorIds().add("CO2-001");

        // Add an initial reading for the temperature sensor
        List<SensorReading> temperatureReadings = new ArrayList<>();
        temperatureReadings.add(new SensorReading(UUID.randomUUID().toString(), System.currentTimeMillis(), 24.5));

        // Add an initial reading for the CO2 sensor
        List<SensorReading> co2Readings = new ArrayList<>();
        co2Readings.add(new SensorReading(UUID.randomUUID().toString(), System.currentTimeMillis(), 420.0));

        readingHistoryStore.put("TEMP-001", temperatureReadings);
        readingHistoryStore.put("CO2-001", co2Readings);
    }

    private InMemoryStore() {
    }
}