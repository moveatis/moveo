package com.moveatis.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationCategorySetList;
import com.moveatis.user.IdentifiedUserEntity;

@Named(value = "feedbackAnalysisCategorySelectionBean")
@ViewScoped
public class FeedbackAnalysisCategorySelectionManagedBean extends AbstractCategorySelectionManagedBean implements Serializable{
	
	 @Override
	 protected void addAllCategorySetsFromEventGroup(ObservationCategorySetList addTo, EventGroupEntity eventGroup) {
		        
		        Set<FeedbackAnalysisCategorySetEntity> categorySets = eventGroup.getFeedbackAnalysisCategorySets();
		        
		        if (categorySets == null) return;
		        
		        for (FeedbackAnalysisCategorySetEntity categorySetEntity : categorySets) {
		            ObservationCategorySet categorySet = new ObservationCategorySet(categorySetEntity.getId(), categorySetEntity.getLabel());
		            Map<Integer, AbstractCategoryEntity> categories = categorySetEntity.getCategoryEntitys();
		            for (AbstractCategoryEntity category : categories.values()) {
		                categorySet.add(observationManagedBean.getNextTag(), category.getLabel().getText());
		            }
		            
		            addTo.add(categorySet);
		        }
		    }
		    
		    /**
		     * Adds all category sets from all the given event groups and puts them in a category set list.
		     */
	 		@Override
		    protected void addAllCategorySetsFromEventGroups(ObservationCategorySetList categorySets, List<EventGroupEntity> eventGroups) {
		        for (EventGroupEntity eventGroup_ : eventGroups) {
		            addAllCategorySetsFromEventGroup(categorySets, eventGroup_);
		        }
		    }
		   
		    
		    /**
		     * Initializes properly all the members needed for category selection.
		     */
		    @PostConstruct
		    public void init() {
		        eventGroup = null;
		        defaultCategorySets = new ObservationCategorySetList();
		        privateCategorySets = new ObservationCategorySetList();
		        categorySetsInUse   = new ObservationCategorySetList();
		        

		        if (observationManagedBean.getEventEntity() != null) {
		            EventEntity event = observationManagedBean.getEventEntity();
		            eventGroup = event.getEventGroup();
		            addAllCategorySetsFromEventGroup(defaultCategorySets, eventGroup);
		        }

		        if (sessionBean.isIdentifiedUser()) {
		            IdentifiedUserEntity user = sessionBean.getLoggedIdentifiedUser();
		            addAllCategorySetsFromEventGroups(privateCategorySets, eventGroupEJB.findAllForOwner(user));
		            }		        
		        List<ObservationCategorySet> categorySets = sessionBean.getCategorySetsInUse();
		        if (categorySets != null) {
		            categorySetsInUse.setCategorySets(categorySets);
		        } else {
		            for(ObservationCategorySet categorySet : defaultCategorySets.getCategorySets()) {
		                categorySetsInUse.addClone(categorySet);
		            }
		        }
		    }
}
