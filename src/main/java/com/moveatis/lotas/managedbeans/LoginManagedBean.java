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
package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.enums.SessionStatus;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.session.SessionBean;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@ManagedBean(name="loginBean")
@RequestScoped
public class LoginManagedBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginManagedBean.class);
    
    private static final String CATEGORY_SELECTION_URL = "/categorySelection/index.xhtml?faces-redirect=true";
    
    @ManagedProperty("#{msg}")
    private ResourceBundle messages;
    
    @Inject
    private Session sessionBean;
    
    private String loginOutcome;
    private String tag;
    
    private String userName;
    private String password;

    /**
     * Creates a new instance of Login
     */
    public LoginManagedBean() {
        
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        if(sessionBean.setTagUser(tag) == SessionStatus.TAG_OK) {
           this.loginOutcome = "taguser";
        } else {
            RequestContext.getCurrentInstance().showMessageInDialog(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), 
                    messages.getString("tagNotFound")));
        }
    }

    public String doTagLogin() {
        return "taguser";
    }
    
    public String doAnonymityLogin() {
        sessionBean.setAnonymityUser();
        return "anonymityuser";
    }
    
    public String doIdentityProviderLogin() {
        if(sessionBean.setIdentityProviderUser(this.userName, this.password) == SessionStatus.USER_OK) {
            this.loginOutcome = "useridentified";
            LOGGER.debug("user identified");
        } else {
            RequestContext.getCurrentInstance().showMessageInDialog(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), 
                    messages.getString("userNotFound")));
            this.loginOutcome = "";
        }
        return this.loginOutcome;
    }    

    public Session getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;
    }

    public String getLoginOutcome() {
        return loginOutcome;
    }

    public void setLoginOutcome(String loginOutcome) {
        this.loginOutcome = loginOutcome;
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