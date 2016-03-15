package com.moveatis.lotas.restful;

import com.moveatis.lotas.enums.UserType;
import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.records.RecordEntity;
import java.io.Serializable;
import java.io.StringReader;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Path("/records")
@Named(value="recordBean")
@Stateful
public class RecordListenerBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordListenerBean.class);
    private static final long serialVersionUID = 1L;
    
    private JsonReader jsonReader;
    
    @EJB(beanName="DebugSessionBean")
    private Session sessionBean;
    
    @EJB
    private Observation observationBean;
    
    public RecordListenerBean() {
        
    }
    
    @POST
    @Path("addrecord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addRecord(RecordEntity record) {
        observationBean.addRecord(record);

        return "Data received ok";
    }
    
    @POST
    @Path("addobservationdata")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addObservationData(String data) {
        
        StringReader stringReader = new StringReader(data);
        jsonReader = Json.createReader(stringReader);
        JsonArray array = jsonReader.readArray();
        jsonReader.close();
        
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.getJsonObject(i);
            
            RecordEntity record = new RecordEntity();
            record.setCategory(object.getString("category"));
            record.setStartTime(object.getJsonNumber("startTime").longValue());
            record.setEndTime(object.getJsonNumber("endTime").longValue());
            
            observationBean.addRecord(record);
        }
        
        return "Data received ok";
    }
    
    @POST
    @Path("updaterecord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateRecord() {
        return "ok";
    }
    
    //For debug purposes
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        if(sessionBean.getUserType() == UserType.IDENTIFIED_USER) {
            return "You are identified, output will be saved to database";
        } else {
            return "You are not identified, output will not be saved to database";
        }
    }
}
