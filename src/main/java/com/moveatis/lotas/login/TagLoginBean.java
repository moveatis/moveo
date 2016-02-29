package com.moveatis.lotas.login;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "tagBasedLoginBean")
@ViewScoped
public class TagLoginBean implements Serializable {
    
    private String loginOutcome = "success";

    /**
     * Creates a new instance of TagLoginBean
     */
    public TagLoginBean() {
        
    }

    public String getLoginOutcome() {
        return loginOutcome;
    }

    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
    }
}
