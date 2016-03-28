package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.application.ApplicationEntity;
import com.moveatis.lotas.interfaces.Application;
import java.util.Calendar;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "applicationManagedBean")
@RequestScoped
public class ApplicationManagedBean {
    
    @Inject
    private Application applicationEJB;
    
    private ApplicationEntity applicationEntity;

    public ApplicationManagedBean() {
        
    }
    
    public Object installApplication() {
        
        applicationEntity = new ApplicationEntity();
        applicationEntity.setApplicationInstalled(Calendar.getInstance().getTime());
        applicationEJB.create(applicationEntity);
        
        if(applicationEJB.find(1L) != null) {
            return "installed";
        }
        
        return "failed";
    }
    
}
