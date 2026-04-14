// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.exceptions;

public class SensorUnavailableException extends RuntimeException {

    // Thrown when a sensor cannot accept readings or is not available for use
    public SensorUnavailableException(String message) {
        super(message);
    }
}