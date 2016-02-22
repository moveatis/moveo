package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "sceneBean")
@SessionScoped
public class SceneManagedBean implements Serializable {

    /**
     * Creates a new instance of SceneManagedBean
     */
    public SceneManagedBean() {
    }
    
}
