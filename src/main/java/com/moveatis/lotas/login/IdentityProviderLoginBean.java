package com.moveatis.lotas.login;

import com.moveatis.lotas.interfaces.Login;
import com.moveatis.lotas.interfaces.Session;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "identityProviderBasedLoginBean")
@ViewScoped
public class IdentityProviderLoginBean implements Login, Serializable {
    
    private static final long serialVersionUID = 1L;
        
    @EJB
    private Session sessionEJB;
    
    private String loginOutcome;
    private String groupKey;

    public IdentityProviderLoginBean() {
        
    }
    
    @Override
    @PostConstruct
    public void init() {
        sessionEJB.setIdentifiedUser();
    }

    @Override
    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
    }

    @Override
    public String getLoginOutcome() {
        return this.loginOutcome;
    }
    
}
