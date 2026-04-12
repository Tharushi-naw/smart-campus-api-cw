// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getApiDetails() {
        Map<String, Object> apiDetails = new LinkedHashMap<>();
        apiDetails.put("apiName", "Smart Campus API");
        apiDetails.put("version", "v1");
        apiDetails.put("adminContact", "smartcampus@westminster.ac.uk");

        Map<String, String> resourceLinks = new LinkedHashMap<>();
        resourceLinks.put("rooms", "/api/v1/rooms");
        resourceLinks.put("sensors", "/api/v1/sensors");

        apiDetails.put("resources", resourceLinks);

        return apiDetails;
    }
}