package com.moveatis.lotas.login;

import com.moveatis.lotas.interfaces.Login;
import com.moveatis.lotas.interfaces.Session;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "anonymityBasedLoginBean")
@ViewScoped
public class AnonymityLoginBean implements Login, Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AnonymityLoginBean.class);

    private static final long serialVersionUID = 1L;    
    
    @EJB
    private Session sessionEJB;
    
    private String loginOutcome;

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

    @Override
    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
    }

    @Override
    public String getLoginOutcome() {
        return this.loginOutcome;
    }
    
    /**
     * TODO: This bean must override the getLoginOutcome
     */
}
