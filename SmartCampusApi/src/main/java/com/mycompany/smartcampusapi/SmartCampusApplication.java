// WATN Nethmi / 20221962 / w1985668

package com.mycompany.smartcampusapi;

import com.mycompany.smartcampusapi.exceptions.GlobalExceptionMapper;
import com.mycompany.smartcampusapi.exceptions.LinkedResourceNotFoundExceptionMapper;
import com.mycompany.smartcampusapi.exceptions.ResourceNotFoundExceptionMapper;
import com.mycompany.smartcampusapi.exceptions.RoomNotEmptyExceptionMapper;
import com.mycompany.smartcampusapi.exceptions.SensorUnavailableExceptionMapper;
import com.mycompany.smartcampusapi.filters.ApiLoggingFilter;
import com.mycompany.smartcampusapi.resources.DiscoveryResource;
import com.mycompany.smartcampusapi.resources.RoomResource;
import com.mycompany.smartcampusapi.resources.SensorResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> registeredClasses = new HashSet<>();

        // Register API resource classes
        registeredClasses.add(DiscoveryResource.class);
        registeredClasses.add(RoomResource.class);
        registeredClasses.add(SensorResource.class);

        // Register custom exception mappers for cleaner error responses
        registeredClasses.add(RoomNotEmptyExceptionMapper.class);
        registeredClasses.add(LinkedResourceNotFoundExceptionMapper.class);
        registeredClasses.add(SensorUnavailableExceptionMapper.class);
        registeredClasses.add(ResourceNotFoundExceptionMapper.class);
        registeredClasses.add(GlobalExceptionMapper.class);

        // Register request/response logging filter
        registeredClasses.add(ApiLoggingFilter.class);

        return registeredClasses;
    }
}