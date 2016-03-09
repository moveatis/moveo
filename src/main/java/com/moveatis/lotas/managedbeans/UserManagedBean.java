package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "userBean")
@SessionScoped
public class UserManagedBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance of UserManagedBean
     */
    public UserManagedBean() {
    }
    
}
