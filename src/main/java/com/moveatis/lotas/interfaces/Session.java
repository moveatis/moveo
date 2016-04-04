package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.enums.SessionStatus;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(Session.class)
public interface Session {
    
    public SessionStatus setTagUser(String tag);
    public SessionStatus setIdentityProviderUser(String userName, String password);
    
}
