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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAnonymityUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTagUser(String tag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUserEntity(UserEntity user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserType getUserType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
