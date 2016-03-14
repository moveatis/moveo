package com.moveatis.lotas.session;

import com.moveatis.lotas.enums.UserType;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.user.UserEntity;
import javax.ejb.Stateful;
import javax.enterprise.inject.Default;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Default
@Stateful
public class DebugSessionBean implements Session {

    @Override
    public void setIdentifiedUser() {
        
    }

    @Override
    public void setAnonymityUser() {
        
    }

    @Override
    public void setTagUser(String tag) {
        
    }

    @Override
    public void setUserEntity(UserEntity user) {
        
    }

    @Override
    public UserType getUserType() {
        return UserType.IDENTIFIED_USER;
    }
}
