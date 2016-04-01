package com.moveatis.lotas.mail;

import com.moveatis.lotas.enums.MailStatus;
import javax.ejb.Stateless;
import com.moveatis.lotas.interfaces.Mailer;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
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
    public MailStatus sendEmail(String recipient, String subject, String message) {
        
        try {
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.host", MAILHOST);
            Session session = Session.getDefaultInstance(props);
            MimeMessage msg = new MimeMessage(session);
            
            msg.setFrom(FROM);
            msg.addRecipients(Message.RecipientType.TO, recipient);
            msg.setSubject(subject);
            msg.setText(message);
            
            Transport.send(msg);
            
            return MailStatus.MAIL_SENT_OK;
        } catch (MessagingException ex) {
           LOGGER.debug(ex.toString());
        }
        
        return MailStatus.MAIL_SENT_FAILED;
    }

}
