package com.moveatis.lotas.application;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.enums.ApplicationStatusCode;
import com.moveatis.lotas.interfaces.Application;
import com.moveatis.lotas.interfaces.User;
import com.moveatis.lotas.user.UserEntity;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class InstallDevelEntitiesBean {
    
    @Inject
    private Application applicationEJB;
    
    @Inject
    private User userEJB;
    
    public InstallDevelEntitiesBean() {
        
    }
    
    public ApplicationStatusCode createDevelEntities() {
        
        if(applicationEJB.find(1L) == null) {
            ApplicationEntity applicationEntity = new ApplicationEntity();
            applicationEntity.setApplicationInstalled(Calendar.getInstance().getTime());
            applicationEJB.create(applicationEntity);
            
            if(applicationEJB.find(1L) == null) {
                return ApplicationStatusCode.INSTALLATION_FAILED;
            }
        }
        
        userEJB.create(createRegularUser());
        UserEntity superUser = createAdminUser();
        userEJB.create(superUser);
        applicationEJB.addSuperUser(superUser);
        
        return ApplicationStatusCode.INSTALLATION_OK;
    }
    
    private UserEntity createRegularUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("Taavi");
        user.setLastName("Testaaja");
        return user;
    }

    private UserEntity createAdminUser() {
        UserEntity admin = new UserEntity();
        admin.setFirstName("Paavo");
        admin.setLastName("Paakayttaja");
        return admin;
    }
    
    private CategoryEntity createCategory() {
        CategoryEntity category = new CategoryEntity();
        return category;
    }
}
