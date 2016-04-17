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

import com.moveatis.application.InstallationBean;
import com.moveatis.enums.ApplicationStatusCode;
import com.moveatis.helpers.PasswordHashGenerator;
import com.moveatis.interfaces.Role;
import com.moveatis.interfaces.Session;
import com.moveatis.interfaces.User;
import com.moveatis.user.IdentifiedUserEntity;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Random;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value="identityProviderBean")
@RequestScoped
public class IdentityProviderRegistrationBean implements IdentityProvider, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;

    private IdentifiedUserEntity userEntity;
    private IdentityProviderInformationEntity identityProviderInformationEntity;
    
    @Inject
    private Role roleBean;
    
    @Inject
    private User userBean;
    
    @Inject
    private InstallationBean installationBean;
    
    @Inject
    private Session sessionEJB;
    
    public IdentityProviderRegistrationBean() {
            
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String registerSuperUser() {
        
        userEntity = new IdentifiedUserEntity();
        identityProviderInformationEntity = new IdentityProviderInformationEntity();
        identityProviderInformationEntity = setPasswordHash(identityProviderInformationEntity);
        identityProviderInformationEntity.setUsername(username);
        
        userEntity.setIdentityProviderInformationEntity(identityProviderInformationEntity);
        
        userEntity.setCreated(Calendar.getInstance().getTime());
        identityProviderInformationEntity.setUserEntity(userEntity);
        userBean.create(userEntity);
        roleBean.addSuperuserRoleToUser(userEntity);
        
        sessionEJB.setIdentityProviderUser(userEntity);
        
        if(installationBean.createApplication() == ApplicationStatusCode.INSTALLATION_OK) {
            return "success";
        } else {
            return "failed";
        }
    }
    
    private IdentityProviderInformationEntity setPasswordHash(IdentityProviderInformationEntity entity) {
        entity.setIterations(generateIterations());
        byte[] salt = generateSalt();
        entity.setSalt(salt);
        
        byte[] passwordHash = hashPassword(password.toCharArray(), entity.getSalt(), entity.getIterations());
        entity.setPasswordHash(passwordHash);

        return entity;
    }
    
    private byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[48];
        secureRandom.nextBytes(salt);
        return salt;
    }
    
    private int generateIterations() {
        Random random = new Random();
        return random.nextInt(16) + 5;
    }
    
    private byte[] hashPassword( final char[] password, final byte[] salt, final int iterations) {
        return PasswordHashGenerator.hashPassword(password, salt, iterations);
   }

    @Override
    public IdentifiedUserEntity getIdentifiedUserEntity() {
        userEntity = new IdentifiedUserEntity();
        identityProviderInformationEntity = new IdentityProviderInformationEntity();
        
        userEntity.setIdentityProviderInformationEntity(identityProviderInformationEntity);
        identityProviderInformationEntity.setUserEntity(userEntity);
        
        return userEntity;
    }

}
