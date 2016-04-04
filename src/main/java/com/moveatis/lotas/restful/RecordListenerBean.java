/*
 * Copyright 2016 Sami Kallio.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * * Neither the name of the University of Jyväskylä nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.lotas.restful;

import com.moveatis.lotas.enums.UserType;
import com.moveatis.lotas.interfaces.Category;
import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.records.RecordEntity;
import com.moveatis.lotas.session.SessionBean;
import java.io.Serializable;
import java.io.StringReader;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
    
    @Context
    private HttpServletRequest httpRequest;
    
    private SessionBean sessionBean;
    
    @EJB
    private Observation observationEJB;
    
    @EJB
    private Category categoryEJB;
    
    public RecordListenerBean() {
        
    }
    
    @POST
    @Path("addrecord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addRecord(RecordEntity record) {
        
        sessionBean = getSessionBean();
        
        observationEJB.addRecord(record);

        return "Data received ok";
    }
    
    @POST
    @Path("addobservationdata")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addObservationData(String data) {
        
        sessionBean = getSessionBean();
        
        StringReader stringReader = new StringReader(data);
        jsonReader = Json.createReader(stringReader);
        JsonObject jObject = jsonReader.readObject();
        JsonArray array = jObject.getJsonArray("data");
        jsonReader.close();
        
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.getJsonObject(i);
            RecordEntity record = new RecordEntity();
            
            /*if(categoryEJB.find(object.getString("category")) != null) {
                record.setCategory(categoryEJB.find(object.getString("category")));
            }*/
                       
            record.setCategory(object.getString("category"));
            record.setStartTime(object.getJsonNumber("startTime").longValue());
            record.setEndTime(object.getJsonNumber("endTime").longValue());
            
            observationEJB.addRecord(record);
        }
        
        return "Data received ok";
    }
    
    @POST
    @Path("updaterecord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateRecord() {
        
        sessionBean = getSessionBean();
        return "ok";
    }
    
    //For debug purposes
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        
        sessionBean = getSessionBean();
        
        if(sessionBean.getUserType() == UserType.IDENTIFIED_USER) {
            return "You are identified, output will be saved to database";
        } else {
            return "You are not identified, output will not be saved to database";
        }
    }
    
    private SessionBean getSessionBean() {
        return (SessionBean)httpRequest.getSession().getAttribute("sessionBean");
    }
}
