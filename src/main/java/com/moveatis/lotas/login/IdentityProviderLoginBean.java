package com.moveatis.lotas.login;

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
public class IdentityProviderLoginBean extends AbstractLogin implements Serializable {
    
    private static final long serialVersionUID = 1L;
        
    @EJB
    private Session sessionEJB;

    /**
     * Creates a new instance of IdentityProviderLoginBean
     */
    public IdentityProviderLoginBean() {
        
    }
    
    @Override
    @PostConstruct
    public void init() {
        sessionEJB.setIdentifiedUser();
    }
    
    /**
     * TODO: This bean must override the getLoginOutcome
     */
    
}
