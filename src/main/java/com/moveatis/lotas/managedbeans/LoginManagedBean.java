package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.enums.SessionStatus;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.session.SessionBean;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@ManagedBean(name="loginBean")
@RequestScoped
public class LoginManagedBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginManagedBean.class);
    
    private static final String CATEGORY_SELECTION_URL = "/categorySelection/index.xhtml?faces-redirect=true";
    
    @ManagedProperty("#{msg}")
    private ResourceBundle messages;
    
    @Inject
    private Session sessionBean;
    
    private String loginOutcome;
    private String tag;
    
    private String userName;
    private String password;

    /**
     * Creates a new instance of Login
     */
    public LoginManagedBean() {
        
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        if(sessionBean.setTagUser(tag) == SessionStatus.TAG_OK) {
           this.loginOutcome = CATEGORY_SELECTION_URL;
        } else {
            RequestContext.getCurrentInstance().showMessageInDialog(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), 
                    messages.getString("tagNotFound")));
        }
    }

    public String doTagLogin() {
        return this.loginOutcome;
    }
    
    public String doAnonymityLogin() {
        return this.loginOutcome;
    }
    
    public String doIdentityProviderLogin() {
        if(sessionBean.setIdentityProviderUser(this.userName, this.password) == SessionStatus.USER_OK) {
            this.loginOutcome = CATEGORY_SELECTION_URL;
            LOGGER.debug("user identified");
        } else {
            RequestContext.getCurrentInstance().showMessageInDialog(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), 
                    messages.getString("userNotFound")));
            this.loginOutcome = "";
        }
        return this.loginOutcome;
    }    

    public Session getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;
    }

    public String getLoginOutcome() {
        return loginOutcome;
    }

    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}