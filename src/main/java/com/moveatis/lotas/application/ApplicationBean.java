package com.moveatis.lotas.application;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.moveatis.lotas.interfaces.Application;
import com.moveatis.lotas.user.UserEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Singleton
@Startup
public class ApplicationBean extends AbstractBean<ApplicationEntity> implements Application {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationBean.class);

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;
    
    private ApplicationEntity applicationEntity;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApplicationBean() {
        super(ApplicationEntity.class);
    }

    @Override
    public boolean checkInstalled() {
        Date installed = super.find(1L).getApplicationInstalled();
        return installed != null;
    }

    @Override
    public void addSuperUser(UserEntity superUser) {
        this.applicationEntity = super.find(1L);
        if(!checkInstalled()) {
            return;
        }
        List<UserEntity> superusers = this.applicationEntity.getSuperUsers();
        if(superusers == null) {
            superusers = new ArrayList<>();
        }
        superusers.add(superUser);
        this.applicationEntity.setSuperUsers(superusers);
        super.edit(this.applicationEntity);
        
    }    
}
