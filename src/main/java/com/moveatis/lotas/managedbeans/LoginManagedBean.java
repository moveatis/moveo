package com.moveatis.lotas.managedbeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginManagedBean {
    
    private Logger logger = LoggerFactory.getLogger(LoginManagedBean.class);
    
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
