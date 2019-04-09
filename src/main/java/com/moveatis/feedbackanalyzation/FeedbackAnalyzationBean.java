package com.moveatis.feedbackanalyzation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.abstracts.AbstractBean;
import com.moveatis.interfaces.Event;
import com.moveatis.interfaces.FeedbackAnalysisRecord;
import com.moveatis.interfaces.FeedbackAnalyzation;
import com.moveatis.observation.ObservationBean;
import com.moveatis.records.FeedbackAnalysisRecordEntity;
import com.moveatis.session.SessionBean;
import com.moveatis.user.AbstractUser;

@Stateful
public class FeedbackAnalyzationBean extends AbstractBean<FeedbackAnalyzationEntity> implements FeedbackAnalyzation,Serializable{
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationBean.class);
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private SessionBean sessionBean;
    
    @EJB
    private Event eventEJB;
    
    private FeedbackAnalyzationEntity feedbackAnalyzationEntity;

    @PersistenceContext(unitName = "MOVEATIS_PERSISTENCE")
    private EntityManager em;

    @Inject
	private FeedbackAnalysisRecord feedbackAnalysisRecordEJB;
        
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeedbackAnalyzationBean() {
        super(FeedbackAnalyzationEntity.class);
    }

    /**
     * Finds and returns all observations for the specific user.
     * @param observer The user, whose observations are to be searched.
     * @return A list of the observations for the user.
     */
    @Override
    public List<FeedbackAnalyzationEntity> findAllByObserver(AbstractUser observer) {
        TypedQuery<FeedbackAnalyzationEntity> query = em.createNamedQuery("findFeedbackAnalyzationsByObserver", FeedbackAnalyzationEntity.class);
        query.setParameter("observer", observer);
        return query.getResultList();
    }

    /**
     * Finds the observations for the user, which have no event attached to them.
     * @param observer The user, whose observations are to be searched.
     * @return A list of the observations.
     */
    @Override
    public List<FeedbackAnalyzationEntity> findWithoutEvent(AbstractUser observer) {
        TypedQuery<FeedbackAnalyzationEntity> query = em.createNamedQuery("findFeedbackAnalyzationsWithoutEvent", FeedbackAnalyzationEntity.class);
        query.setParameter("observer", observer);
        return query.getResultList();
    }

    /**
     * Finds the observations that are made for events that the specified
     * user does not own.
     * @param observer The user, whose observations are to be searched.
     * @return A list of the observations.
     */
    @Override
    public List<FeedbackAnalyzationEntity> findByEventsNotOwned(AbstractUser observer) {
        TypedQuery<FeedbackAnalyzationEntity> query = em.createNamedQuery("findFeedbackAnalyzationsByEventsNotOwned", FeedbackAnalyzationEntity.class);
        query.setParameter("observer", observer);
        return query.getResultList();
    }

    /**
     * Persists the observations to the database.
     * @param feedbackAnalyzation The observatio entity to be persisted.
     */
    @Override
    public void create(FeedbackAnalyzationEntity feedbackAnalyzation) {
        super.create(feedbackAnalyzation);
    }
    
    @Override
    public void edit(FeedbackAnalyzationEntity feedbackAnalyzation) {
        super.edit(feedbackAnalyzation);
    }

    /**
     * Finds a list of the records for the observation with the given id.
     * @param id The id of the observation.
     * @return A list of the records.
     */
    @Override
    public List<FeedbackAnalysisRecordEntity> findRecords(Object id) {
        feedbackAnalyzationEntity = em.find(FeedbackAnalyzationEntity.class, id);
        if(feedbackAnalyzationEntity != null) {
            return feedbackAnalyzationEntity.getRecords();
        } 
        return new ArrayList<>(); //return empty list
    }
    
    /**
     * Removes the observation and also removes the observation from the event
     * it was associated with.
     * @param feedbackAnalyzationEntity The observation to be removed.
     */
    @Override
    public void remove(FeedbackAnalyzationEntity feedbackAnalyzationEntity) {
        super.remove(feedbackAnalyzationEntity);
        eventEJB.removeFeedbackAnalyzation(feedbackAnalyzationEntity);
        feedbackAnalyzationEntity.setEvent(null);
        super.edit(feedbackAnalyzationEntity);
    }
    
    @Override
    public void removeRecordFromAnalyzation(FeedbackAnalyzationEntity feedbackAnalyzation,FeedbackAnalysisRecordEntity record) {
    	List<FeedbackAnalysisRecordEntity> records=feedbackAnalyzation.getRecords();
    	records.remove(record);
    	record.setFeedbackAnalyzation(null);
    	feedbackAnalyzation.setRecords(records);
    	feedbackAnalysisRecordEJB.remove(record);
    	super.edit(feedbackAnalyzation);
    }

    /**
     * Permanently removes the observation, which the user did not set to be
     * saved into the database.
     * @param feedbackAnalyzationEntity The observation to be removed.
     */
    @Override
    public void removeUnsavedObservation(FeedbackAnalyzationEntity feedbackAnalyzationEntity) {
        em.remove(em.merge(feedbackAnalyzationEntity));
    }
}
