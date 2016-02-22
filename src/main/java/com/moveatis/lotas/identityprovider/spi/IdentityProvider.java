package com.moveatis.lotas.identityprovider.spi;

import com.moveatis.lotas.user.UserEntity;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public interface IdentityProvider {
    
    public UserEntity getUserEntity();
    public String getServiceProviderName();
    public void setServiceProviderName(String name);
}
