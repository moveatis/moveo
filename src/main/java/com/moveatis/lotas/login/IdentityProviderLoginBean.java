package com.moveatis.lotas.login;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "identityProviderBasedLoginBean")
@ViewScoped
public class IdentityProviderLoginBean implements Serializable {
    
    private String loginOutcome = "success";

    /**
     * Creates a new instance of IdentityProviderLoginBean
     */
    public IdentityProviderLoginBean() {
        
    }

    public String getLoginOutcome() {
        return loginOutcome;
    }

    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
    }
    
}
