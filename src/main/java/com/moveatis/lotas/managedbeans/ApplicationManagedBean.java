package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.application.InstallDevelEntitiesBean;
import com.moveatis.lotas.enums.ApplicationStatusCode;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@ManagedBean(name="applicationManagedBean")
@RequestScoped
public class ApplicationManagedBean {

    @Inject
    private InstallDevelEntitiesBean installDevelEntitiesEJB;

    public ApplicationManagedBean() {
        
    }
    
    public Object installApplication() {
        
        if(installDevelEntitiesEJB.createDevelEntities() == ApplicationStatusCode.INSTALLATION_OK) {
            return "installed";
        } else {
            return "failed";
        }
    }
    
}
