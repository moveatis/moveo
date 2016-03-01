package com.moveatis.lotas.login;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "anonymityBasedLoginBean")
@ViewScoped
public class AnonymityLoginBean implements Serializable {
    
    private String loginOutcome = "success";

    /**
     * Creates a new instance of AnonymityLoginBean
     */
    public AnonymityLoginBean() {
        
    }

    public String getLoginOutcome() {
        return loginOutcome;
    }

    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
    }
}
