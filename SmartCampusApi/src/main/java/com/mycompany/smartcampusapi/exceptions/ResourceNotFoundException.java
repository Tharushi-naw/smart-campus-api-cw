// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    // Thrown when a requested resource cannot be found in the system
    public ResourceNotFoundException(String message) {
        super(message);
    }
}