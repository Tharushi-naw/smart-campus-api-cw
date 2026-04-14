// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.resources;

import com.mycompany.smartcampusapi.exceptions.LinkedResourceNotFoundException;
import com.mycompany.smartcampusapi.exceptions.ResourceNotFoundException;
import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.store.InMemoryStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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

        // Return all sensors if no type filter is provided
        if (isBlank(requestedType)) {
            return Response.ok(sensorList).build();
        }

        // Filter sensors by the requested type
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

        // Throw a custom exception if the sensor cannot be found
        if (foundSensor == null) {
            throw new ResourceNotFoundException("Sensor with ID " + sensorId + " was not found.");
        }

        return Response.ok(foundSensor).build();
    }

    @POST
    public Response createSensor(Sensor newSensor, @Context UriInfo uriInfo) {
        // Validate the incoming sensor payload
        if (newSensor == null) {
            throw new IllegalArgumentException("Sensor data is required.");
        }

        if (isBlank(newSensor.getSensorId())
                || isBlank(newSensor.getSensorType())
                || isBlank(newSensor.getRoomId())) {
            throw new IllegalArgumentException("sensorId, sensorType and roomId are required.");
        }

        synchronized (InMemoryStore.sensorStore) {
            // Prevent duplicate sensor IDs
            if (InMemoryStore.sensorStore.containsKey(newSensor.getSensorId())) {
                throw new IllegalStateException("A sensor with this sensorId already exists.");
            }

            // Make sure the room exists before linking the sensor to it
            Room linkedRoom = InMemoryStore.roomStore.get(newSensor.getRoomId());
            if (linkedRoom == null) {
                throw new LinkedResourceNotFoundException("The specified roomId does not exist.");
            }

            // Default status is ACTIVE if nothing is provided
            if (isBlank(newSensor.getSensorStatus())) {
                newSensor.setSensorStatus("ACTIVE");
            }

            InMemoryStore.sensorStore.put(newSensor.getSensorId(), newSensor);
            linkedRoom.getSensorIds().add(newSensor.getSensorId());

            // Start an empty reading history for the new sensor
            InMemoryStore.readingHistoryStore.put(newSensor.getSensorId(), new ArrayList<>());
        }

        URI sensorUri = uriInfo.getAbsolutePathBuilder()
                .path(newSensor.getSensorId())
                .build();

        return Response.created(sensorUri)
                .entity(newSensor)
                .build();
    }

    // Route reading-related requests to the sub-resource
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    // Utility method for checking blank string values
    private boolean isBlank(String textValue) {
        return textValue == null || textValue.trim().isEmpty();
    }
}