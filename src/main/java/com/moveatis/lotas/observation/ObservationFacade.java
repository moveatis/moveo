package com.moveatis.lotas.observation;

import com.moveatis.lotas.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class ObservationFacade extends AbstractFacade<ObservationEntity> implements ObservationFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservationFacade() {
        super(ObservationEntity.class);
    }
    
}
