package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.user.UserEntity;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface Session {
    
    public void setIdentifiedUser();
    public void setAnonymityUser();
    public void setTagUser(String tag);
    public void setUserEntity(UserEntity user);
    
    
}
