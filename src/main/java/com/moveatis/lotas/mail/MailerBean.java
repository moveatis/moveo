package com.moveatis.lotas.mail;

import com.moveatis.lotas.enums.MailStatus;
import javax.ejb.Stateless;
import com.moveatis.lotas.interfaces.Mailer;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
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
    private static final String FROM = "lotas@jyu.fi";
    

    public MailerBean() {
        
    }

    @Override
    public MailStatus sendEmail(final String[] recipients, final String subject, final String message) {
        
        try {
            MimeMessage msg = setMessage(recipients, subject);
            msg.setText(message);
            
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
            
            Multipart multipart = new MimeMultipart();
            MimeBodyPart msgPart = new MimeBodyPart();
            msgPart.setContent(msg, "text/html");
            multipart.addBodyPart(msgPart);
            
            for(File f:attachmentFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(f);
                multipart.addBodyPart(attachPart);
            }
            
            msg.setContent(multipart);
            Transport.send(msg);
            return MailStatus.MAIL_SENT_OK;
            
        } catch(MessagingException ex) {
            LOGGER.debug(ex.toString());
        } catch (IOException ex) {
            LOGGER.debug(ex.toString());
        }
        
        return MailStatus.MAIL_SENT_FAILED;
    }
    
    private MimeMessage setMessage(final String[] recipients, final String subject) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", MAILHOST);
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        
        msg.setFrom(FROM);
        for(String recipient:recipients) {
            msg.addRecipients(Message.RecipientType.TO, recipient);
        }
        msg.setSubject(subject);

        return msg;
    }
}
