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
package com.moveatis.managedbeans;

import com.moveatis.interfaces.MessageBundle;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.hibernate.validator.constraints.Email;

/**
 * Bean that validates user input.
 * @author ilkrpaan
 */
@Named(value = "validationBean")
@RequestScoped
public class ValidationManagedBean {
    
    @Inject @MessageBundle
    private transient ResourceBundle messages;

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Creates a new instance of ValidationManagedBean
     */
    public ValidationManagedBean() {
    }
    
    private void throwError(String message) {
        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), message));
    }
    
    public void validateCategoryName(FacesContext context, UIComponent component, Object value) {
        // TODO: Maybe check only categories that have been selected?
        // NOTE: Validation causes problems when one tries to e.g.
        // delete invalid category or add new category when invalid categories are present!
        validateStringForJsAndHtml(context, component, value);
        
        // TODO: User should be allowed to continue even if some categories are empty!
        // Confirmation dialog(?) ==> Maybe validation isn't the right place for this...
        validateNotEmpty(context, component, value);
    }
    
    // NOTE: Some ui components have "required" attribute which does something similar.
    public void validateNotEmpty(FacesContext context, UIComponent component, Object value) {
        String s = ((String)value).trim();
        if (s == null || s.isEmpty()) {
            // TODO: Proper message!
            throwError("Kategoria pitää nimetä.");
        }
    }
    
    public void validateStringForJsAndHtml(FacesContext context, UIComponent component, Object value) {
        String s = (String)value;
        for (int i = 0; i < s.length(); ) {
            int codePoint = s.codePointAt(i);
            if (!Character.isLetterOrDigit(codePoint)
                    && (codePoint != " ".codePointAt(0))) {
                // TODO: Proper message!
                throwError("Vain kirjaimet, numerot ja välilyönnit ovat sallittuja.");
            }
            i += Character.charCount(codePoint);
        }
    }

    public void validateShortString(FacesContext context, UIComponent component, Object value) {
        validateStringForJsAndHtml(context, component, value);
        validateStringMaxLength((String) value, 64);
    }

    public void validateLongString(FacesContext context, UIComponent component, Object value) {
        validateStringForJsAndHtml(context, component, value);
        validateStringMaxLength((String) value, 256);
    }

    private void validateStringMaxLength(String str, int maxLength) {
        if (str.length() > maxLength) {
            String error = MessageFormat.format(messages.getString("validate_lengthExceeded"), maxLength);
            throwError(error);
        }
    }

}
