package com.moveatis.lotas.restful;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    public NotFoundExceptionMapper() {
        
    }

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response
                .status(Status.NOT_FOUND)
                .entity("HTTP 404 - Not found")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
