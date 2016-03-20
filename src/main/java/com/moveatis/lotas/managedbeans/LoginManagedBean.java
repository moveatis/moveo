package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.session.SessionBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginManagedBean.class);
    
    private static final String OBSERVER_URL = "/observer/index.xhtml?faces-redirect=true";
    
    //@ManagedProperty(value = "#{sessionBean}")
    @Inject
    private SessionBean sessionBean;
    
    private String loginOutcome;
    private String tag;

    /**
     * Creates a new instance of Login
     */
    public LoginManagedBean() {
        
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        if(tag.isEmpty()) {
            
        } else {
        
            this.tag = tag;

            LOGGER.debug("Tag -> " + tag);
            sessionBean.setTagUser(tag);

            this.loginOutcome = OBSERVER_URL;
        }
    }

    public String doTagLogin() {
        return this.loginOutcome;
    }
    
    public String doAnonymityLogin() {
        return this.loginOutcome;
    }
    
    public String doIdentityProviderLogin() {
        return this.loginOutcome;
    }    

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}