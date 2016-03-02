package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.facade.SceneFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "sceneBean")
@SessionScoped
public class SceneManagedBean implements Serializable {
    
    @Inject
    private SceneFacadeLocal sceneEJB;
    
    private List<String> categories;
    
    /**
     * Creates a new instance of SceneManagedBean
     */
    public SceneManagedBean() {
        
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategories() {
        categories = sceneEJB.getCategories();
        return categories;
    }
    
    //
    // NOTE: This isn't probably the right managed bean for adding recordings...
    //
    
    private String recording = "no recording";
    
    public void addRecording() {
        String startTime = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("start-time");
        String endTime = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("end-time");
        recording = startTime + " --> " + endTime;
    }
    
    public String getRecording() {
        return recording;
    }
}
