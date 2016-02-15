package com.moveatis.lotas;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author sammarju
 */
@Named(value = "loginBean")
@RequestScoped
public class Login {
    
    private String etunimi = "Testi"; 

    /**
     * Creates a new instance of Login
     */
    public Login() {
        
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }
    
    
    
}
