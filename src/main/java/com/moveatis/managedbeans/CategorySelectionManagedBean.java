/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.managedbeans;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.helpers.Validation;
import com.moveatis.interfaces.EventGroup;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Session;
import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationCategorySetList;
import com.moveatis.user.IdentifiedUserEntity;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.faces.view.ViewScoped;

/**
 * The bean that serves the category selection view.
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Ilari Paananen <ilari.k.paananen at student.jyu.fi>
 */
@Named(value = "categorySelectionBean")
@ViewScoped
public class CategorySelectionManagedBean extends AbstractCategorySelectionManagedBean implements Serializable {

		

		    
		    /**
		     * Adds all category sets from given event group to given observation category set list.
		     */
	 @Override
	 protected void addAllCategorySetsFromEventGroup(ObservationCategorySetList addTo, EventGroupEntity eventGroup) {
		        
		        Set<CategorySetEntity> categorySets = eventGroup.getCategorySets();
		        
		        if (categorySets == null) return;
		        
		        for (CategorySetEntity categorySetEntity : categorySets) {
		            ObservationCategorySet categorySet = new ObservationCategorySet(categorySetEntity.getId(), categorySetEntity.getLabel());
		            Map<Integer, AbstractCategoryEntity> categories = categorySetEntity.getCategoryEntitys();
		            for (AbstractCategoryEntity category : categories.values()) {
		                categorySet.add(((CategoryEntity)category).getCategoryType(), observationManagedBean.getNextTag(), category.getLabel().getText());
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
