// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.resources;

import com.mycompany.smartcampusapi.exceptions.ResourceNotFoundException;
import com.mycompany.smartcampusapi.exceptions.RoomNotEmptyException;
import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.store.InMemoryStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
        // Validate the incoming room data before saving it
        if (newRoom == null) {
            throw new IllegalArgumentException("Room data is required.");
        }

        if (isBlank(newRoom.getRoomId()) || isBlank(newRoom.getRoomName())) {
            throw new IllegalArgumentException("roomId and roomName are required.");
        }

        synchronized (InMemoryStore.roomStore) {
            // Prevent duplicate room IDs
            if (InMemoryStore.roomStore.containsKey(newRoom.getRoomId())) {
                throw new IllegalStateException("A room with this roomId already exists.");
            }

            // Make sure sensorIds is always initialized
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
            throw new ResourceNotFoundException("Room with ID " + roomId + " was not found.");
        }

        return Response.ok(foundRoom).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        synchronized (InMemoryStore.roomStore) {
            Room roomToDelete = InMemoryStore.roomStore.get(roomId);

            if (roomToDelete == null) {
                throw new ResourceNotFoundException("Room with ID " + roomId + " was not found.");
            }

            // Do not allow deleting a room that still has sensors linked to it
            if (roomToDelete.getSensorIds() != null && !roomToDelete.getSensorIds().isEmpty()) {
                throw new RoomNotEmptyException("Room cannot be deleted because sensors are still assigned to it.");
            }

            InMemoryStore.roomStore.remove(roomId);
        }

        return Response.noContent().build();
    }

    // Simple helper method for blank string validation
    private boolean isBlank(String textValue) {
        return textValue == null || textValue.trim().isEmpty();
    }
}