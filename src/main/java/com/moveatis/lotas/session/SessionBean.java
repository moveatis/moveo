package com.moveatis.lotas.session;

import com.moveatis.lotas.enums.UserType;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.user.UserEntity;
import javax.ejb.Stateful;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
public class SessionBean implements Session {

    private UserType userType;
    private String tag;
    private UserEntity userEntity;
    
    public SessionBean() {
        
    }
    
    @Override
    public void setIdentifiedUser() {
        userType = UserType.IDENTIFIED_USER;
    }
    
    @Override
    public void setAnonymityUser() {
        userType = UserType.ANONYMITY_USER;
    }
    
    @Override
    public void setTagUser(String tag) {
        userType = UserType.TAG_USER;
        this.tag = tag;
    }

    @Override
    public UserType getUserType() {
        return userType;
    }

    @Override
    public void setUserEntity(UserEntity user) {
        this.userEntity = user;
    }
    
    
}
