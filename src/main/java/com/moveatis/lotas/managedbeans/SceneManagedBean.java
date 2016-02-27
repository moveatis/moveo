package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import com.moveatis.lotas.interfaces.Scene;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "sceneBean")
@SessionScoped
public class SceneManagedBean implements Serializable {
    
    @EJB
    private Scene sceneEJB;
    
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
}
