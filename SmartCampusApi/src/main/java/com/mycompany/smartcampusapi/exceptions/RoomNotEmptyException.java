// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.exceptions;

public class RoomNotEmptyException extends RuntimeException {

    // Thrown when trying to delete a room that still has sensors assigned
    public RoomNotEmptyException(String message) {
        super(message);
    }
}