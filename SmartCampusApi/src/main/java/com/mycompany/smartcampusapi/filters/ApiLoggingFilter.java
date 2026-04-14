// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.filters;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // Logger used to track incoming requests and outgoing responses
    private static final Logger logger = Logger.getLogger(ApiLoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Log basic request details before the resource method runs
        logger.info("--- Incoming Request ---");
        logger.info("Method: " + requestContext.getMethod());
        logger.info("URI: " + requestContext.getUriInfo().getAbsolutePath());
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        // Log the response status after the request has been processed
        logger.info("--- Outgoing Response ---");
        logger.info("Status: " + responseContext.getStatus());
    }
}