package com.moveatis.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.Session;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationCategorySetList;
import com.moveatis.user.IdentifiedUserEntity;

@Named(value = "feedbackAnalysisCategorySelectionManagedBean")
@ViewScoped
public class FeedbackAnalysisCategorySelectionManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String newCategorySetName;
    
    private FeedbackAnalysisCategorySetEntity selectedDefaultCategorySet;
    private FeedbackAnalysisCategorySetEntity selectedPrivateCategorySet;
    
    private Set<FeedbackAnalysisCategorySetEntity> defaultCategorySets; // From group key or event that was selected in control page.
    private Set<FeedbackAnalysisCategorySetEntity>  privateCategorySets;
    private Set<FeedbackAnalysisCategorySetEntity>  categorySetsInUse;
    
    private EventGroupEntity eventGroup;
    
    @Inject
    private Session sessionEJB;
    @Inject
    private EventGroup eventGroupEJB;
	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;
	
	@PostConstruct
	public void init() {
		eventGroup = null;

        
        if (feedbackAnalyzationManagedBean.getEventEntity() != null) {
            EventEntity event = feedbackAnalyzationManagedBean.getEventEntity();
            eventGroup = event.getEventGroup();
            defaultCategorySets=eventGroup.getFeedbackAnalysisCategorySets();
        }

        if (sessionEJB.isIdentifiedUser()) {
            IdentifiedUserEntity user = sessionEJB.getLoggedIdentifiedUser();
            for (EventGroupEntity eg: eventGroupEJB.findAllForOwner(sessionEJB.getLoggedInUser()))
            	for (FeedbackAnalysisCategorySetEntity fba:eg.getFeedbackAnalysisCategorySets())
            		privateCategorySets.add(fba);
        }
        
        Set<FeedbackAnalysisCategorySetEntity> categorySets = sessionEJB.getFeedbackAnalysisCategorySetsInUse();
        if (categorySets != null) {
            categorySetsInUse=categorySets;
        } else {
            for(FeedbackAnalysisCategorySetEntity categorySet : defaultCategorySets) {
                categorySetsInUse.add(categorySet);
            }
        }
    }
	}

