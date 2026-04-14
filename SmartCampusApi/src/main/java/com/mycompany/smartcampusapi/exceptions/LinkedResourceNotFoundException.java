// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.exceptions;

public class LinkedResourceNotFoundException extends RuntimeException {

    // Thrown when a related resource does not exist
    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}