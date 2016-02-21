package com.moveatis.lotas.identityprovider;

import com.moveatis.lotas.identityprovider.spi.IdentityProvider;
import com.moveatis.lotas.user.UserEntity;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import javax.inject.Singleton;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Singleton
public class IdentityProviderService {
    
    
    private static IdentityProviderService service;
    private ServiceLoader<IdentityProvider> loader;
    
    private IdentityProviderService() {
        loader = ServiceLoader.load(IdentityProvider.class);
    }
    
    public static synchronized IdentityProviderService getInstance() {
        if(service == null) {
            service = new IdentityProviderService();
        }
        return service;
    }
    
    public UserEntity getUser(String serviceProviderName) {
        
        UserEntity user = null;
        IdentityProvider identityProvider = null;
        
        try {
            Iterator<IdentityProvider> providers = loader.iterator();
            
            while(identityProvider == null && providers.hasNext()) {
                IdentityProvider ip = providers.next();
                if(ip.getServiceProviderName().equals(serviceProviderName)) {
                    identityProvider = ip;
                }
            }
            
            if(identityProvider != null) {
                user = identityProvider.getUserEntity();
            }
        } catch (ServiceConfigurationError error){
            
        }
        
        return user;
    }
    
}
