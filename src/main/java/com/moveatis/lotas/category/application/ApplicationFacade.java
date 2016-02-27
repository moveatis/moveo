package com.moveatis.lotas.category.application;

import com.moveatis.lotas.facade.AbstractFacade;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.facade.ApplicationFacadeLocal;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
@Singleton
@Startup
public class ApplicationFacade extends AbstractFacade<ApplicationEntity> implements ApplicationFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApplicationFacade() {
        super(ApplicationEntity.class);
    }
    
}
