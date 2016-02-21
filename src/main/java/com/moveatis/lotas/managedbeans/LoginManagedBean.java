package com.moveatis.lotas.managedbeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginManagedBean {
    
    private String etunimi = "Testi"; 

    /**
     * Creates a new instance of Login
     */
    public LoginManagedBean() {
        
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }
    
    
    
}
