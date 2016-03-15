package com.moveatis.lotas.login;

import com.moveatis.lotas.interfaces.Login;
import com.moveatis.lotas.interfaces.Session;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "tagBasedLoginBean")
@ViewScoped
public class TagLoginBean implements Login, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Session sessionEJB;
    private String tag;


    public TagLoginBean() {
        
    }

    @Override
    @PostConstruct
    public void init() {
        
    }

    @Override
    public void setLoginOutcome(String loginOutcome) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLoginOutcome() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
