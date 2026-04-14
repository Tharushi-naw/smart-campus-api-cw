// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.exceptions;

import com.mycompany.smartcampusapi.models.ErrorMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    // Logger used to record unexpected server-side errors
    private static final Logger logger = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Log the full error for debugging and server-side tracking
        logger.log(Level.SEVERE, "Unexpected server error", exception);

        // Return a safe JSON response instead of exposing internal details
        ErrorMessage errorMessage = new ErrorMessage(
                "An unexpected internal server error occurred.",
                500,
                "/api/v1/docs/errors#internal-server-error"
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}