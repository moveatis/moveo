package com.moveatis.records;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.moveatis.abstracts.AbstractBean;
import com.moveatis.interfaces.FeedbackAnalysisRecord;
@Stateless
public class FeedbackAnalysisRecordBean extends AbstractBean<FeedbackAnalysisRecordEntity> implements FeedbackAnalysisRecord {


	@PersistenceContext(unitName = "MOVEATIS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeedbackAnalysisRecordBean() {
        super(FeedbackAnalysisRecordEntity.class);
    }
    
}
