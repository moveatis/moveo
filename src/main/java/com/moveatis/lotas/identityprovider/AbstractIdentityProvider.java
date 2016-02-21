package com.moveatis.lotas.identityprovider;

import java.net.URL;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public abstract class AbstractIdentityProvider {
    
    protected String identityProviderUserId;
    protected String identityProviderName;
    protected URL identityProviderOAUTHUri;
    protected URL clientRedirectUri;
    
}
