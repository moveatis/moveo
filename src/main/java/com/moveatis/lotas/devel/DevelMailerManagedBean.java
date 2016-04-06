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
package com.moveatis.lotas.devel;

import com.moveatis.lotas.enums.MailStatus;
import com.moveatis.lotas.export.FileBuilder;
import com.moveatis.lotas.interfaces.Mailer;
import java.io.File;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;

/**
 * MailerManagedBean
 * testing for sending mail in development.
 * 
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value="mailerManagedBean")
@RequestScoped
public class DevelMailerManagedBean {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DevelMailerManagedBean.class);
    
    @Inject
    private Mailer mailerEJB;
    
    private String recipient;
    private String subject;
    private String text;
    private String fileName;

    public DevelMailerManagedBean() {
        
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void sendEmail(ActionEvent ae) {
        if(mailerEJB.sendEmail(this.recipient.split(","), this.subject, this.text) == MailStatus.MAIL_SENT_OK) {
            FacesContext.getCurrentInstance().addMessage(recipient, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Sähköposti lähetetty", "Sähköposti lähetetty onnistuneesti"));
        } else {
            FacesContext.getCurrentInstance().addMessage(recipient, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Sähköposti jäi lähettämättä", "Sähköpostin lähetys epäonnistui"));
        }
        
    }
    
    public void sendEmailWithAttachment(ActionEvent ae) {
        
        try {
            File[] files = new File[1];
            files[0] = new FileBuilder().constructFile(this.fileName);
            
            if(mailerEJB.sendEmailWithAttachment(this.recipient.split(","), this.subject, this.text, 
                    files) == MailStatus.MAIL_SENT_OK) {
                FacesContext.getCurrentInstance().addMessage(recipient, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Sähköposti liitteineen lähetetty", "Sähköposti liitteineen lähetetty onnistuneesti"));
            } else {
                FacesContext.getCurrentInstance().addMessage(recipient, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Sähköposti liitteinen jäi lähettämättä", "Sähköpostin ja liitteiden lähetys epäonnistui"));
            }
        } catch (IOException ex) {
            LOGGER.debug(ex.toString());
        }
    }
}
