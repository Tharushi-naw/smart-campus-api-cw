// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.resources;

import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.store.InMemoryStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Response getAllRooms() {
        // Return all rooms currently stored in memory
        List<Room> roomList = new ArrayList<>(InMemoryStore.roomStore.values());
        return Response.ok(roomList).build();
    }

    @POST
    public Response createRoom(Room newRoom, @Context UriInfo uriInfo) {
        // Basic null check for the incoming request body
        if (newRoom == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createMessageBody("Room data is required."))
                    .build();
        }

        // roomId and roomName are required fields
        if (isBlank(newRoom.getRoomId()) || isBlank(newRoom.getRoomName())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createMessageBody("roomId and roomName are required."))
                    .build();
        }

        synchronized (InMemoryStore.roomStore) {
            // Prevent duplicate room IDs
            if (InMemoryStore.roomStore.containsKey(newRoom.getRoomId())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(createMessageBody("A room with this roomId already exists."))
                        .build();
            }

            // Make sure sensorIds is never left null
            if (newRoom.getSensorIds() == null) {
                newRoom.setSensorIds(new ArrayList<>());
            }

            InMemoryStore.roomStore.put(newRoom.getRoomId(), newRoom);
        }

        // Build the URI for the newly created room
        URI roomUri = uriInfo.getAbsolutePathBuilder()
                .path(newRoom.getRoomId())
                .build();

        return Response.created(roomUri)
                .entity(newRoom)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room foundRoom = InMemoryStore.roomStore.get(roomId);

        if (foundRoom == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createMessageBody("Room with ID " + roomId + " was not found."))
                    .build();
        }

        return Response.ok(foundRoom).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        synchronized (InMemoryStore.roomStore) {
            Room roomToDelete = InMemoryStore.roomStore.get(roomId);

            if (roomToDelete == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createMessageBody("Room with ID " + roomId + " was not found."))
                        .build();
            }

            // Do not allow deletion if sensors are still linked to the room
            if (roomToDelete.getSensorIds() != null && !roomToDelete.getSensorIds().isEmpty()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(createMessageBody("Room cannot be deleted because sensors are still assigned to it."))
                        .build();
            }

            InMemoryStore.roomStore.remove(roomId);
        }

        return Response.noContent().build();
    }

    // Small helper method to return consistent message responses
    private Map<String, String> createMessageBody(String messageText) {
        Map<String, String> messageBody = new LinkedHashMap<>();
        messageBody.put("message", messageText);
        return messageBody;
    }

    // Utility method for simple blank string validation
    private boolean isBlank(String textValue) {
        return textValue == null || textValue.trim().isEmpty();
    }
}