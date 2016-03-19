package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com
 */
@Named(value = "superUserBean")
@SessionScoped
public class SuperUserManagedBean implements Serializable {

    public SuperUserManagedBean() {
    }
    
}
