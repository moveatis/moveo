package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.enums.MailStatus;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface Mailer {

    MailStatus sendEmail(String recipient, String subject, String message);
    
}
