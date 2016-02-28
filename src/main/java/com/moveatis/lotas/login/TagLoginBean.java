package com.moveatis.lotas.login;

import com.moveatis.lotas.interfaces.Session;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "tagBasedLoginBean")
@ViewScoped
public class TagLoginBean extends AbstractLogin implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Session sessionEJB;
    private String tag;

    /**
     * Creates a new instance of TagLoginBean
     */
    public TagLoginBean() {
        
    }

    @Override
    public void init() {
        
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        sessionEJB.setTagUser(tag);
    }
}
