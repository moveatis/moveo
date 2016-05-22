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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements Mailer-interface, and takes care of mailing the users
 * the requested information.
 * 
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class MailerBean implements Mailer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MailerBean.class);
    
    private static final String MAILHOST = "localhost";
    private static final String FROM = "donotreply@moveatis.sport.jyu.fi";
    
    private static final String CHARSET = "UTF-8";
    

    public MailerBean() {
        
    }

    /**
     * Sends mail to recipients.
     * 
     * @param recipients List of users who should receive the mail
     * @param subject Subject of the mail
     * @param message The message for the mail
     * @return enum which states if the mail sent failed or succeeded.
     */
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
            LOGGER.error("Error in email", ex);
        }
        
        return MailStatus.MAIL_SENT_FAILED;
    }

    /**
     * Method for sending mail with attachments.
     * 
     * @param recipients Array of users who should receive the mail
     * @param subject Subject of the mail
     * @param message The message for the mail
     * @param attachmentFiles  Array of files to be attached to the mail
     * @return enum which states if the mail sent failed or succeeded.
     */
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
            LOGGER.error("Error in email", ex);
        } 
        
        return MailStatus.MAIL_SENT_FAILED;
    }
    
    /**
     * This method creates the mimemessage, which is sent to the recipients
     * 
     * @param recipients Array of users who should receive the mail
     * @param subject Subject of the mail
     * @param message The message for the mail
     * @return The MimeMessage containing the necessary information for sending the email
     * @throws MessagingException If there is an error in sending the email
     */
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
