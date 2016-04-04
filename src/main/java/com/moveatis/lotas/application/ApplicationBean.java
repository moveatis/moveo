package com.moveatis.lotas.application;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.moveatis.lotas.interfaces.Application;
import com.moveatis.lotas.user.UserEntity;
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
        try {
            findApplicationEntity();
            List<UserEntity> superusers = this.applicationEntity.getSuperUsers();
            superusers.add(superUser);
            applicationEntity.setSuperUsers(superusers);
            super.edit(applicationEntity);
        } catch(NullPointerException npe) {
            LOGGER.debug("Nullpointer");
        }
        
    }
    
    private void findApplicationEntity() {
        try {
            this.applicationEntity = super.find(1L);
            if(this.applicationEntity == null) {
                this.applicationEntity = new ApplicationEntity();
                super.create(this.applicationEntity);
            }
            LOGGER.debug("applicationEntity löytyi");
        } catch(NullPointerException npe) {
            LOGGER.debug("applicationEntitya ei ole vielä luotu");
            this.applicationEntity = new ApplicationEntity();
            em.persist(this.applicationEntity);
        }
    }
    
}
