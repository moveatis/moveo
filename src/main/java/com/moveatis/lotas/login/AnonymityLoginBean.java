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
@Named(value = "anonymityBasedLoginBean")
@ViewScoped
public class AnonymityLoginBean extends AbstractLogin implements Serializable {

    private static final long serialVersionUID = 1L;    
    
    @EJB
    private Session sessionEJB;

    /**
     * Creates a new instance of AnonymityLoginBean
     */
    public AnonymityLoginBean() {
        
    }
    
    @PostConstruct
    @Override
    public void init() {
        sessionEJB.setAnonymityUser();
    }
    
    /**
     * TODO: This bean must override the getLoginOutcome
     */
}
