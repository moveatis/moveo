package com.moveatis.lotas.restful;

import java.io.Serializable;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Path("/records")
@Named(value="recordBean")
@Stateful
public class RecordListenerBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public RecordListenerBean() {
        
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addRecord() {
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateRecord() {
        
    }
    
    //For debug purposes
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello, world!";
    }
    
}
