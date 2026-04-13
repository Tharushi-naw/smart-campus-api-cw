// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.resources;

import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.models.SensorReading;
import com.mycompany.smartcampusapi.store.InMemoryStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadingHistory() {
        // Check whether the sensor exists before returning its readings
        Sensor linkedSensor = InMemoryStore.sensorStore.get(sensorId);

        if (linkedSensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createMessageBody("Sensor with ID " + sensorId + " was not found."))
                    .build();
        }

        // Get the reading history, or create an empty list if none exists yet
        List<SensorReading> readingHistory = InMemoryStore.readingHistoryStore.get(sensorId);
        if (readingHistory == null) {
            readingHistory = new ArrayList<>();
            InMemoryStore.readingHistoryStore.put(sensorId, readingHistory);
        }

        return Response.ok(readingHistory).build();
    }

    @POST
    public Response addReading(SensorReading newReading, @Context UriInfo uriInfo) {
        // Make sure the sensor exists before adding a reading
        Sensor linkedSensor = InMemoryStore.sensorStore.get(sensorId);

        if (linkedSensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createMessageBody("Sensor with ID " + sensorId + " was not found."))
                    .build();
        }

        // Block new readings if the sensor is under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(linkedSensor.getSensorStatus())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(createMessageBody("This sensor is under maintenance and cannot accept new readings."))
                    .build();
        }

        // Validate request body
        if (newReading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createMessageBody("Reading data is required."))
                    .build();
        }

        // Auto-generate a reading ID if one is not provided
        if (newReading.getReadingId() == null || newReading.getReadingId().trim().isEmpty()) {
            newReading.setReadingId(UUID.randomUUID().toString());
        }

        // Use the current system time if timestamp is missing
        if (newReading.getTimestamp() == 0) {
            newReading.setTimestamp(System.currentTimeMillis());
        }

        List<SensorReading> readingHistory = InMemoryStore.readingHistoryStore.get(sensorId);
        if (readingHistory == null) {
            readingHistory = new ArrayList<>();
            InMemoryStore.readingHistoryStore.put(sensorId, readingHistory);
        }

        readingHistory.add(newReading);

        // Update the sensor's current value with the latest reading
        linkedSensor.setCurrentValue(newReading.getValue());

        URI readingUri = uriInfo.getAbsolutePathBuilder()
                .path(newReading.getReadingId())
                .build();

        return Response.created(readingUri)
                .entity(newReading)
                .build();
    }

    // Helper method for simple JSON message responses
    private Map<String, String> createMessageBody(String messageText) {
        Map<String, String> messageBody = new LinkedHashMap<>();
        messageBody.put("message", messageText);
        return messageBody;
    }
}