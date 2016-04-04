package com.moveatis.lotas.session;

import com.moveatis.lotas.enums.SessionStatus;
import com.moveatis.lotas.enums.UserType;
import com.moveatis.lotas.interfaces.Application;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.interfaces.User;
import com.moveatis.lotas.user.UserEntity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@SessionScoped
public class SessionBean implements Serializable, Session  {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionBean.class);
    
    @EJB
    private Application applicationEJB;
    @EJB
    private User userEJB;

    private UserType userType;
    private String tag;
    
    private UserEntity userEntity;
    
    private Boolean loggedIn = false;
    
    public SessionBean() {
        
    }

    @Override
    public SessionStatus setIdentityProviderUser(String userName, String password) {
        userType = UserType.IDENTIFIED_USER;
        if("admin".equals(userName) && "admin".equals(password)) {
            this.userEntity = userEJB.findByName("Paavo", "Paakayttaja");
            this.loggedIn = true;
            return SessionStatus.USER_OK;
        } else if("user".equals(userName) && "user".equals(password)) {
            this.userEntity = userEJB.findByName("Taavi", "Testaaja");
            this.loggedIn = true;
            return SessionStatus.USER_OK;
        }
        return SessionStatus.USER_NOT_FOUND;
    }
    
    @Override
    public SessionStatus setAnonymityUser() {
        userType = UserType.ANONYMITY_USER;
        return SessionStatus.USER_OK;
        
    }
    
    @Override
    public SessionStatus setTagUser(String tag) {
        if(tag == null) {
            return SessionStatus.TAG_NOT_FOUND;
        }
        userType = UserType.TAG_USER;
        this.loggedIn = true;
        this.tag = tag;
        return SessionStatus.TAG_NOT_FOUND;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserEntity(UserEntity user) {
        this.userEntity = user;
    }

    public Boolean isLoggedIn() {
        return loggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.loggedIn = isLoggedIn;
    }
    
    @Override
    public String toString() {
        return "SessionBean: userType -> " + getUserType() + ", loggedIn -> " + isLoggedIn();
    }
        
}
