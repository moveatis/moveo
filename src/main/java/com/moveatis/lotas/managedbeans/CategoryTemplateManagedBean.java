package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "categoryTemplateBean")
@SessionScoped
public class CategoryTemplateManagedBean implements Serializable {

    /**
     * Creates a new instance of CategoryTemplateManagedBean
     */
    public CategoryTemplateManagedBean() {
    }
    
}
