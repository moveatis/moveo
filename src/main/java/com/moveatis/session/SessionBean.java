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
package com.moveatis.session;

import com.moveatis.enums.SessionStatus;
import com.moveatis.enums.UserType;
import com.moveatis.interfaces.Application;
import com.moveatis.interfaces.Session;
import com.moveatis.interfaces.User;
import com.moveatis.timezone.TimeZoneInformation;
import com.moveatis.user.AbstractUser;
import com.moveatis.user.IdentifiedUserEntity;
import com.moveatis.user.TagUserEntity;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@SessionScoped
@Named
public class SessionBean implements Serializable, Session  {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionBean.class);
    
    @EJB
    private Application applicationEJB;
    
    @EJB
    private User userEJB;

    private UserType userType;
    private String tag;
    
    private SortedSet<Long> sessionObservations;
    
    private IdentifiedUserEntity userEntity;
    private TagUserEntity tagEntity;
    private AbstractUser abstractUser;
    
    private boolean loggedIn = false;
    private boolean identifiedUser = false;
    private boolean saveable = false;

    private TimeZone sessionTimeZone = TimeZoneInformation.getTimeZone();

    public SessionBean() {

    }
    
    @Override
    public SessionStatus setIdentityProviderUser(IdentifiedUserEntity user) {
        userType = UserType.IDENTIFIED_USER;
        this.userEntity = user;
        this.abstractUser = user;
        this.loggedIn = true;
        return SessionStatus.USER_OK;
    }
    
    @Override
    public SessionStatus setAnonymityUser() {
        
        userType = UserType.ANONYMITY_USER;
        this.loggedIn = true;
        return SessionStatus.USER_OK;
        
    }
    
    @Override
    public SessionStatus setTagUser(TagUserEntity tagUser) {
        if(tagUser == null) {
            return SessionStatus.TAG_NOT_FOUND;
        }
        userType = UserType.TAG_USER;
        this.loggedIn = true;
        this.tagEntity = tagUser;
        this.abstractUser = tagUser;
        
        return SessionStatus.TAG_NOT_FOUND;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public boolean isIdentifiedUser() {
        return userType == UserType.IDENTIFIED_USER;
    }

    @Override
    public String toString() {
        return "SessionBean: userType -> " + getUserType() + ", loggedIn -> " + isLoggedIn();
    }

    @Override
    public SortedSet<Long> getSessionObservationsIds() {
        if(this.sessionObservations == null) {
            return new TreeSet<>();
        }
        return this.sessionObservations;
    }

    @Override
    public boolean isSaveable() {
        return this.sessionObservations != null;
    }

    @Override
    public void setSessionObservations(SortedSet<Long> observationsIds) {
        this.sessionObservations = observationsIds;
    }        

    @Override
    public AbstractUser getLoggedInUser() {
        return this.abstractUser;
    }

    @Override
    public TimeZone getSessionTimeZone() {
        return sessionTimeZone;
    }

    @Override
    public void setSessionTimeZone(TimeZone timeZone) {
        this.sessionTimeZone = timeZone;
    }

    @Override
    public IdentifiedUserEntity getLoggedIdentifiedUser() {
        return this.userEntity;
    }
}
