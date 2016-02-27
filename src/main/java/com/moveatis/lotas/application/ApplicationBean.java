package com.moveatis.lotas.application;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import com.moveatis.lotas.interfaces.Application;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
@Singleton
@Startup
public class ApplicationBean extends AbstractBean<ApplicationEntity> implements Application {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApplicationBean() {
        super(ApplicationEntity.class);
    }
    
}
