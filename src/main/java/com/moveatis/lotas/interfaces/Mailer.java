package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.enums.MailStatus;
import java.io.File;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface Mailer {

    MailStatus sendEmail(final String[] recipients, final String subject, final String message);
    MailStatus sendEmailWithAttachment(final String[] recipient, final String subject, final String message, final File[] attachmentFile);
    
}
