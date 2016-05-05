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
package com.moveatis.mail;

import com.moveatis.enums.MailStatus;
import javax.ejb.Stateless;
import com.moveatis.interfaces.Mailer;
import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class MailerBean implements Mailer {
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MailerBean.class);
    
    private static final String MAILHOST = "localhost";
    private static final String FROM = "donotreply@moveatis.sport.jyu.fi";
    
    private static final String CHARSET = "UTF-8";
    

    public MailerBean() {
        
    }

    @Override
    public MailStatus sendEmail(final String[] recipients, final String subject, final String message) {
        
        try {
            MimeMessage msg = setMessage(recipients, subject);
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message, CHARSET);
            
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            
            msg.setContent(multipart);
            Transport.send(msg);
            
            return MailStatus.MAIL_SENT_OK;
        } catch (MessagingException ex) {
           LOGGER.debug(ex.toString());
        }
        
        return MailStatus.MAIL_SENT_FAILED;
    }

    @Override
    public MailStatus sendEmailWithAttachment(final String[] recipients, final String subject, final String message, final File[] attachmentFiles) {
        
        try {
            MimeMessage msg = setMessage(recipients,subject);
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message, CHARSET);
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            
            for(File f:attachmentFiles) {
                DataSource source = new FileDataSource(f);
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.setDataHandler(new DataHandler(source));
                attachPart.setFileName(f.getName());
                
                multipart.addBodyPart(attachPart);
            }
            
            msg.setContent(multipart);
            Transport.send(msg);
            
            return MailStatus.MAIL_SENT_OK;
            
        } catch(MessagingException ex) {
            LOGGER.debug(ex.toString());
        } 
        
        return MailStatus.MAIL_SENT_FAILED;
    }
    
    private MimeMessage setMessage(final String[] recipients, final String subject) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", MAILHOST);
        props.setProperty("mail.mime.charset", CHARSET);
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        
        msg.setFrom(FROM);
        for(String recipient:recipients) {
            msg.addRecipients(Message.RecipientType.TO, recipient);
        }

        msg.setSubject(subject, CHARSET);

        return msg;
    }
}
