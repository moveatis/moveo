package com.moveatis.lotas.login;

import com.moveatis.lotas.interfaces.Login;
import com.moveatis.lotas.interfaces.Session;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "tagBasedLoginBean")
@ViewScoped
public class TagLoginBean implements Login, Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TagLoginBean.class);
    
    private static final long serialVersionUID = 1L;
    
    private Session sessionEJB;
    private String tag;
    
    private String loginOutcome;


    public TagLoginBean() {
        
    }

    @Override
    @PostConstruct
    public void init() {
        
    }

    @Override
    public void setLoginOutcome(String loginOutcome) {
        LOGGER.debug("Loginoutcome -> " + loginOutcome);
    }

    @Override
    public String getLoginOutcome() {
        return loginOutcome;
    }
}
