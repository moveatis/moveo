package com.moveatis.managedbeans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.interfaces.Category;
import com.moveatis.interfaces.CategorySet;
import com.moveatis.interfaces.FeedbackAnalysisRecord;
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
    private List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse;

    private int numberOfRecords;
    private int currentRecordNumber;
    @Inject
    private CategorySet categorySetEJB;
    
    private FeedbackAnalysisRecordEntity currentRecord;
    
    private String comment;
    
    public void setCurrentRecord(int recordNumber) {
    	if(recordNumber>numberOfRecords||recordNumber<1)return;
    	currentRecordNumber=recordNumber;
    	currentRecord=feedbackAnalyzationEntity.getRecords().get(recordNumber-1);
        for(FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
        	for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
        		((FeedbackAnalysisCategoryEntity)fac).setInRecord(false);
        comment=currentRecord.getComment();
        		
    	List<FeedbackAnalysisCategoryEntity> selectedCategories=currentRecord.getSelectedCategories();
        for(FeedbackAnalysisCategoryEntity category:selectedCategories)
        	category.setInRecord(true);
    }



    public List<FeedbackAnalysisCategorySetEntity> getFeedbackAnalysisCategorySetsInUse() {
		return feedbackAnalysisCategorySetsInUse;
	}

	public void setFeedbackAnalysisCategorySetsInUse(List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse) {
		this.feedbackAnalysisCategorySetsInUse = feedbackAnalysisCategorySetsInUse;
	}

	private EventEntity eventEntity;
    
    @EJB
    private FeedbackAnalyzation feedbackAnalyzationEJB;
    @Inject
    private Session sessionBean;


    public FeedbackAnalyzationManagedBean() {
        
    }
    
    @PostConstruct
    public void init() {
    	setCurrentRecordNumber(1);
    	setNumberOfRecords(1);
    	startObservation();
        currentRecord=new FeedbackAnalysisRecordEntity();
        currentRecord.setFeedbackAnalyzation(feedbackAnalyzationEntity);
        feedbackAnalyzationEntity.addRecord(currentRecord);
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
    public List<FeedbackAnalysisCategorySetEntity> getCategorySetsInUse() {
        return feedbackAnalysisCategorySetsInUse;
    }

    /**
     * Sets the observation categories to be used in the observation.
     */
    public void setCategorySetsInUse(List<FeedbackAnalysisCategorySetEntity> categorySetsInUse) {
        

        this.feedbackAnalysisCategorySetsInUse = categorySetsInUse;
    }


    
    public void setObservationName(String name) {
        this.feedbackAnalyzationEntity.setName(name);
    }
    
    public void setObservationDuration(long duration) {
        this.feedbackAnalyzationEntity.setDuration(duration);
    }
    

    public void addRecord() {
    	if(feedbackAnalyzationEntity==null)feedbackAnalyzationEntity=new FeedbackAnalyzationEntity();
    	List<FeedbackAnalysisRecordEntity> records = feedbackAnalyzationEntity.getRecords();
        
        if(records == null) {
            records = new ArrayList<>();
        }
        List<FeedbackAnalysisCategoryEntity> selectedCategories=new ArrayList<FeedbackAnalysisCategoryEntity>();
        for(FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
        	for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
        		if(((FeedbackAnalysisCategoryEntity)fac).getInRecord()) {
        			selectedCategories.add((FeedbackAnalysisCategoryEntity) fac);
        			((FeedbackAnalysisCategoryEntity)fac).setInRecord(false);
        		}
        			
        
        currentRecord.setSelectedCategories(selectedCategories);
        
        currentRecord.setComment(comment);
        feedbackAnalyzationEntity.setRecords(records);
        
        comment="";
        currentRecord=new FeedbackAnalysisRecordEntity();
        currentRecord.setFeedbackAnalyzation(feedbackAnalyzationEntity);
        feedbackAnalyzationEntity.addRecord(currentRecord);
        currentRecord.setSelectedCategories(new ArrayList<FeedbackAnalysisCategoryEntity>());
        
        numberOfRecords++;
       	currentRecordNumber++;
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
    public void saveFeedbackAnalyzation() {
        feedbackAnalyzationEntity.setUserWantsToSaveToDatabase(true);
        
        for(FeedbackAnalysisCategorySetEntity categorySet : feedbackAnalysisCategorySetsInUse)
        	if(categorySet.getId()==null)categorySetEJB.create(categorySet);
        
        feedbackAnalyzationEJB.create(feedbackAnalyzationEntity);
    }

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public int getCurrentRecordNumber() {
		return currentRecordNumber;
	}

	public void setCurrentRecordNumber(int currentRecordNumber) {
		this.currentRecordNumber = currentRecordNumber;
	}



	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}
	


}
