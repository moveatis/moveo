/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.identityprovider;

import com.moveatis.helpers.PasswordHashGenerator;
import com.moveatis.interfaces.Session;
import com.moveatis.user.IdentifiedUserEntity;
import java.util.Arrays;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */


@Named(value = "identityProviderLoginBean")
@RequestScoped
public class IdentityProviderLoginBean {
    
    private String userName;
    private String password;
    
    private IdentifiedUserEntity userEntity;
    
    @Inject
    private Session sessionBean;
    
    @Inject
    private IdentityProviderBean ipBean;

    /**
     * Creates a new instance of IdentityProviderLoginBean
     */
    public IdentityProviderLoginBean() {
        
    }
    
    public String doIdentityProviderLogin() {
        IdentityProviderInformationEntity ipInformationEntity = ipBean.findIpEntityByUsername(userName);
        byte[] salt = ipInformationEntity.getSalt();
        int iterations = ipInformationEntity.getIterations();
        byte[] hash = ipInformationEntity.getPasswordHash();
        
        byte[] suppliedPasswordHash = PasswordHashGenerator.hashPassword(password.toCharArray(), salt, iterations);
        
        if(Arrays.equals(suppliedPasswordHash, hash)) {
            userEntity = ipInformationEntity.getIdentifiedUserEntity();
            sessionBean.setIdentityProviderUser(userEntity);
            return "useridentified";
        }
        
        return "failed";
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
