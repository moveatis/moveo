package com.moveatis.lotas.records;

import com.moveatis.lotas.interfaces.AbstractBean;
import com.moveatis.lotas.interfaces.Record;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class RecordBean extends AbstractBean<RecordEntity> implements Record {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecordBean() {
        super(RecordEntity.class);
    }
    
}
