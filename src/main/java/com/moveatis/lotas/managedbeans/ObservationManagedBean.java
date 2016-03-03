package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Map;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "observationBean")
@SessionScoped
public class ObservationManagedBean implements Serializable {

    /**
     * Creates a new instance of ObservationManagedBean
     */
    public ObservationManagedBean() {
    }
    
    //
    // NOTE: Is this the right managed bean for adding recordings?
    //
    
    private String recording = "no recording";
    
    public void addRecording() {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String startTime = requestParameters.get("start-time");
        String endTime = requestParameters.get("end-time");
        recording = startTime + " --> " + endTime;
    }
    
    public String getRecording() {
        return recording;
    }
}
