// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.resources;

import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.store.InMemoryStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getAllSensors(@QueryParam("type") String requestedType) {
        // Get all sensors from the in-memory store
        List<Sensor> sensorList = new ArrayList<>(InMemoryStore.sensorStore.values());

        // If no type is provided, return the full list
        if (isBlank(requestedType)) {
            return Response.ok(sensorList).build();
        }

        // Filter sensors by type when a query parameter is given
        List<Sensor> filteredSensors = new ArrayList<>();
        for (Sensor currentSensor : sensorList) {
            if (currentSensor.getSensorType() != null
                    && currentSensor.getSensorType().equalsIgnoreCase(requestedType)) {
                filteredSensors.add(currentSensor);
            }
        }

        return Response.ok(filteredSensors).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor foundSensor = InMemoryStore.sensorStore.get(sensorId);

        if (foundSensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createMessageBody("Sensor with ID " + sensorId + " was not found."))
                    .build();
        }

        return Response.ok(foundSensor).build();
    }

    @POST
    public Response createSensor(Sensor newSensor, @Context UriInfo uriInfo) {
        // Check that the request body exists
        if (newSensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createMessageBody("Sensor data is required."))
                    .build();
        }

        // Required fields for creating a sensor
        if (isBlank(newSensor.getSensorId())
                || isBlank(newSensor.getSensorType())
                || isBlank(newSensor.getRoomId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createMessageBody("sensorId, sensorType and roomId are required."))
                    .build();
        }

        synchronized (InMemoryStore.sensorStore) {
            // Prevent duplicate sensor IDs
            if (InMemoryStore.sensorStore.containsKey(newSensor.getSensorId())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(createMessageBody("A sensor with this sensorId already exists."))
                        .build();
            }

            // Make sure the sensor is linked to an existing room
            Room linkedRoom = InMemoryStore.roomStore.get(newSensor.getRoomId());
            if (linkedRoom == null) {
                return Response.status(422)
                        .entity(createMessageBody("The specified roomId does not exist."))
                        .build();
            }

            // Set a default status if one is not provided
            if (isBlank(newSensor.getSensorStatus())) {
                newSensor.setSensorStatus("ACTIVE");
            }

            InMemoryStore.sensorStore.put(newSensor.getSensorId(), newSensor);
            linkedRoom.getSensorIds().add(newSensor.getSensorId());

            // Start an empty reading history for the new sensor
            InMemoryStore.readingHistoryStore.put(newSensor.getSensorId(), new ArrayList<>());
        }

        // Build the URI of the newly created sensor
        URI sensorUri = uriInfo.getAbsolutePathBuilder()
                .path(newSensor.getSensorId())
                .build();

        return Response.created(sensorUri)
                .entity(newSensor)
                .build();
    }

    // Helper method for sending simple JSON messages
    private Map<String, String> createMessageBody(String messageText) {
        Map<String, String> messageBody = new LinkedHashMap<>();
        messageBody.put("message", messageText);
        return messageBody;
    }

    // Utility method for checking null or empty strings
    private boolean isBlank(String textValue) {
        return textValue == null || textValue.trim().isEmpty();
    }
}