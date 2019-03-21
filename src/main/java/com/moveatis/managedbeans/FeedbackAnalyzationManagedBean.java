package com.moveatis.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.event.EventEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.interfaces.FeedbackAnalyzation;
import com.moveatis.interfaces.Observation;
import com.moveatis.interfaces.Session;
import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;
import com.moveatis.records.RecordEntity;

@Named(value = "feedbackAnalyzationManagedBean")
@SessionScoped
public class FeedbackAnalyzationManagedBean implements Serializable{
	 
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationManagedBean.class);
    
    private static final long serialVersionUID = 1L;
    
    private FeedbackAnalyzationEntity feedbackAnalyzationEntity;
    private Set<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse;
    private EventEntity eventEntity;
    
    @EJB
    private FeedbackAnalyzation feedbackAnalyzationEJB;
    @Inject
    private Session sessionBean;
    
    // Tag is used to identify the observationcategories within a observation
    private Long nextTag;

    public FeedbackAnalyzationManagedBean() {
        
    }
    
    @PostConstruct
    public void init() {
        nextTag = 0L;
    }
    
    /**
     * Removes the observations the user doesn't want to save to database
     * when the session timeout happens and the bean is destroyed.
     */
    @PreDestroy
    public void destroy() {
        if(feedbackAnalyzationEntity != null) {
            if(!feedbackAnalyzationEntity.getUserWantsToSaveToDatabase()) {
                feedbackAnalyzationEJB.removeUnsavedObservation(feedbackAnalyzationEntity);
            }
        }
    }
    
    public void resetCategorySetsInUse() {
        this.feedbackAnalysisCategorySetsInUse = null;
    }
    
    public void setEventEntity(EventEntity eventEntity) {
        this.eventEntity = eventEntity;
    }
    
    public EventEntity getEventEntity() {
        return this.eventEntity;
    }
    
    /**
     * Creates a new observation entity and initializes it to be used in a new
     * observation.
     */
    public void startObservation() {
        this.feedbackAnalyzationEntity = new FeedbackAnalyzationEntity();
        // Can we use created time for observation start time?
        this.feedbackAnalyzationEntity.setCreated();
        this.feedbackAnalyzationEntity.setEvent(eventEntity);
        // Summary view doesn't break if no records are added.
        // TODO: Should observer not let user continue, if there are no records?
        if(feedbackAnalyzationEntity.getRecords() == null) {
            feedbackAnalyzationEntity.setRecords(new ArrayList<FeedbackAnalysisRecordEntity>());
        }
    }

    /**
     * Returns the current observation entity.
     */
    public FeedbackAnalyzationEntity getObservationEntity() {
        return feedbackAnalyzationEntity;
    }

    /**
     * Sets the current observation entity.
     */
    public void setObservationEntity(FeedbackAnalyzationEntity feedbackAnalyzationEntity) {
        this.feedbackAnalyzationEntity = feedbackAnalyzationEntity;
    }

    /**
     * Gets the observation categories to be used in the observation.
     */
    public Set<FeedbackAnalysisCategorySetEntity> getCategorySetsInUse() {
        return feedbackAnalysisCategorySetsInUse;
    }

    /**
     * Sets the observation categories to be used in the observation.
     */
    public void setCategorySetsInUse(Set<FeedbackAnalysisCategorySetEntity> categorySetsInUse) {
        

        this.feedbackAnalysisCategorySetsInUse = categorySetsInUse;
    }

    public Long getNextTag() {
        return nextTag++;
    }
    
    public void setObservationName(String name) {
        this.feedbackAnalyzationEntity.setName(name);
    }
    
    public void setObservationDuration(long duration) {
        this.feedbackAnalyzationEntity.setDuration(duration);
    }
    
    /**
     * Adds a record to the observation.
     * @param record The record to be added to the observation.
     */
    public void addRecord(FeedbackAnalysisRecordEntity record) {
        List<FeedbackAnalysisRecordEntity> records = feedbackAnalyzationEntity.getRecords();
        record.setFeedbackAnalyzation(feedbackAnalyzationEntity);
        
        if(records == null) {
            records = new ArrayList<>();
        }
        
        records.add(record);
        feedbackAnalyzationEntity.setRecords(records);
    }
    
    /**
     * The method is called from REST API to save the records to the observation.
     */
    public void saveObservation() {
        if (sessionBean.isIdentifiedUser()) {
            feedbackAnalyzationEntity.setObserver(sessionBean.getLoggedIdentifiedUser());
        }
        /*
        NOTE:   GroupKey couldn't be removed if there were observations whose
                observer was the TagUser corresponding to the GroupKey.
                Solution: don't set observer if not identified user.
        
        else { TODO: Can we leave observer unset? Should we ensure it is null or...?
            observationEntity.setObserver(sessionBean.getLoggedInUser());
        }*/
        
        /*
        * Client wanted that user has the ability to persist observation into 
        * database when he/she wants to. Since it was easier to build business logic
        * by creating the observationEntity when observation is done, the boolean flag
        * "userWantsToSaveToDatabase was added. This flag is true if user wants to save
        * the observation to database. If its false, we need remove this observation later.
        */
        feedbackAnalyzationEntity.setUserWantsToSaveToDatabase(false); 
        feedbackAnalyzationEJB.create(feedbackAnalyzationEntity);
    }
    
    /**
     * The method saves the observation to the database.
     */
    public void saveObservationToDatabase() {
        feedbackAnalyzationEntity.setUserWantsToSaveToDatabase(true);
        feedbackAnalyzationEJB.edit(feedbackAnalyzationEntity);
    }
}
