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

import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.groupkey.GroupKeyEntity;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Ilari Paananen <ilari.k.paananen at student.jyu.fi>
 */
@Named(value = "categorySelectionBean")
@ViewScoped
public class CategorySelectionManagedBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategorySelectionManagedBean.class);
    private static final long serialVersionUID = 1L;
    
    private String newCategorySetName;
    
    private Long selectedDefaultCategorySet;
    private Long selectedPublicCategorySet;
    private Long selectedPrivateCategorySet;
    
    private ObservationCategorySetList defaultCategorySets; // From group key or event that was selected in control page.
    private ObservationCategorySetList publicCategorySets;
    private ObservationCategorySetList privateCategorySets;
    private ObservationCategorySetList categorySetsInUse;
    
    private EventGroupEntity eventGroup;
    
    @Inject
    private Session sessionBean;
    
    @Inject
    private EventGroup eventGroupEJB;
    
    @Inject
    private ObservationManagedBean observationManagedBean;

    @Inject @MessageBundle //created MessageBundle to allow resourcebundle injection to CDI beans
    private transient ResourceBundle messages;  //RequestBundle is not serializable 
    
    private Long addedCategorySetTag = 0L;
    
    /**
     * Creates a new instance of CategoryManagedBean
     */
    public CategorySelectionManagedBean() {
    }
    
    private void addAllCategorySetsFromEventGroup(ObservationCategorySetList addTo, EventGroupEntity eventGroup) {
        
        Set<CategorySetEntity> categorySets = eventGroup.getCategorySets();
        
        if (categorySets == null) return;
        
        for (CategorySetEntity categorySetEntity : categorySets) {
            ObservationCategorySet categorySet = new ObservationCategorySet(categorySetEntity.getId(), categorySetEntity.getLabel());
            
            Map<Integer, CategoryEntity> categories = categorySetEntity.getCategoryEntitys();
            for (CategoryEntity category : categories.values()) {
                categorySet.add(category.getCategoryType(), observationManagedBean.getNextTag(), category.getLabel().getLabel());
            }
            
            addTo.add(categorySet);
        }
    }
    
    private ObservationCategorySetList getAllCategorySetsFromEventGroup(EventGroupEntity eventGroup) {
        ObservationCategorySetList categorySets = new ObservationCategorySetList();
        addAllCategorySetsFromEventGroup(categorySets, eventGroup);
        if (categorySets.getCategorySets().isEmpty())
            return null;
        return categorySets;
    }
    
    private ObservationCategorySetList getAllCategorySetsFromEventGroups(List<EventGroupEntity> eventGroups) {
        ObservationCategorySetList categorySets = new ObservationCategorySetList();
        for (EventGroupEntity eventGroup_ : eventGroups) {
            addAllCategorySetsFromEventGroup(categorySets, eventGroup_);
        }
        if (categorySets.getCategorySets().isEmpty())
            return null;
        return categorySets;
    }
    
    @PostConstruct
    public void init() {
        eventGroup = null;
        
        publicCategorySets = getAllCategorySetsFromEventGroups(eventGroupEJB.findAllForPublicUser());
        
        if (sessionBean.isTagUser()) {
            GroupKeyEntity groupKey = sessionBean.getGroupKey();
            eventGroup = groupKey.getEventGroup();
            defaultCategorySets = getAllCategorySetsFromEventGroup(eventGroup);
        } else if (sessionBean.getIsEventEntityForObservationSet()) {
            EventEntity event = sessionBean.getEventEntityForNewObservation();
            eventGroup = event.getEventGroup();
            defaultCategorySets = getAllCategorySetsFromEventGroup(eventGroup);
        }

        if (sessionBean.isIdentifiedUser()) {
            IdentifiedUserEntity user = sessionBean.getLoggedIdentifiedUser();
            privateCategorySets = getAllCategorySetsFromEventGroups(eventGroupEJB.findAllForOwner(user));
        }
        
        categorySetsInUse = new ObservationCategorySetList();
        
        List<ObservationCategorySet> categorySets = sessionBean.getCategorySetsInUse();
        if (categorySets != null) {
            categorySetsInUse.setCategorySets(categorySets);
        } else if (defaultCategorySets != null) {
            for(ObservationCategorySet categorySet : defaultCategorySets.getCategorySets()) {
                categorySetsInUse.addClone(categorySet);
            }
        }
    }
    
    public String getNewCategorySetName() {
        return newCategorySetName;
    }
    
    public void setNewCategorySetName(String newCategorySetName) {
        this.newCategorySetName = newCategorySetName;
    }
    
    public Long getSelectedDefaultCategorySet() {
        return selectedDefaultCategorySet;
    }
    
    public void setSelectedDefaultCategorySet(Long selectedDefaultCategorySet) {
        this.selectedDefaultCategorySet = selectedDefaultCategorySet;
    }
    
    public Long getSelectedPublicCategorySet() {
        return selectedPublicCategorySet;
    }
    
    public void setSelectedPublicCategorySet(Long selectedPublicCategorySet) {
        this.selectedPublicCategorySet = selectedPublicCategorySet;
    }
    
    public Long getSelectedPrivateCategorySet() {
        return selectedPrivateCategorySet;
    }
    
    public void setSelectedPrivateCategorySet(Long selectedPrivateCategorySet) {
        this.selectedPrivateCategorySet = selectedPrivateCategorySet;
    }
    
    public List<ObservationCategorySet> getDefaultCategorySets() {
        return defaultCategorySets.getCategorySets();
    }
    
    public List<ObservationCategorySet> getPublicCategorySets() {
        return publicCategorySets.getCategorySets();
    }
    
    public List<ObservationCategorySet> getPrivateCategorySets() {
        return privateCategorySets.getCategorySets();
    }
    
    public List<ObservationCategorySet> getCategorySetsInUse() {
        return categorySetsInUse.getCategorySets();
    }
    
    public boolean isEventGroupNotNull() {
        return (eventGroup != null);
    }
    
    public String getEventGroupName() {
        if (isEventGroupNotNull())
            return eventGroup.getLabel();
        LOGGER.debug("Cannot get event group name: event group is null! (This should never happen!)");
        return "";
    }
    
    public boolean isDefaultCategorySetsNotNull() {
        return (defaultCategorySets != null);
    }
    
    public boolean isPublicCategorySetsNotNull() {
        return (publicCategorySets != null);
    }
    
    public boolean isPrivateCategorySetsNotNull() {
        return (privateCategorySets != null);
    }
    
    public void addNewCategorySet() {
        String name = Validation.validateForJsAndHtml(newCategorySetName);
        if (!name.isEmpty()) {
            ObservationCategorySet categorySet = new ObservationCategorySet(addedCategorySetTag++, name);
            categorySetsInUse.add(categorySet);
            newCategorySetName = "";
        }
    }
    
    public void addDefaultCategorySet() {
        if (categorySetsInUse.find(selectedDefaultCategorySet) == null) {
            ObservationCategorySet categorySet = defaultCategorySets.find(selectedDefaultCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected default category set not found!");
        }
    }
    
    public void addPublicCategorySet() {
        if (categorySetsInUse.find(selectedPublicCategorySet) == null) {
            ObservationCategorySet categorySet = publicCategorySets.find(selectedPublicCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected public category set not found!");
        }
    }
    
    public void addPrivateCategorySet() {
        if (categorySetsInUse.find(selectedPrivateCategorySet) == null) {
            ObservationCategorySet categorySet = privateCategorySets.find(selectedPrivateCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected private category set not found!");
        }
    }
    
    public void removeCategorySet(ObservationCategorySet categorySet) {
        categorySetsInUse.remove(categorySet);
    }
    
    private void showErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), message));
    }
    
    public String checkCategories() {
        boolean atLeastOneCategorySelected = false;
        
        for (ObservationCategorySet categorySet : categorySetsInUse.getCategorySets()) {
            
            List<ObservationCategory> categories = categorySet.getCategories();
            
            if(hasDuplicate(categories)) {
                showErrorMessage(messages.getString("cs_errorNotUniqueCategories"));
                return "";
            }
            
            if (!categories.isEmpty()) {
                atLeastOneCategorySelected = true;
            }
            
            for (ObservationCategory category : categories) {
                
                if (category.getName().isEmpty()) {
                    showErrorMessage(messages.getString("cs_warningEmptyCategories"));
                    return ""; // TODO: Show confirmation or something and let user continue.
                }
            }
        }
        
        if (!atLeastOneCategorySelected) {
            showErrorMessage(messages.getString("cs_errorNoneSelected"));
            return "";
        }
        
        observationManagedBean.setCategorySetsInUse(categorySetsInUse.getCategorySets());
        return "categoriesok";
    }
    
    private static <T> boolean hasDuplicate(List<ObservationCategory> categories) {
        Set<String> set = new HashSet<>();
        for (ObservationCategory category : categories) {
            String name = category.getName();
            if (!name.isEmpty() && !set.add(category.getName())) {
                return true;
            }
        }
        return false;
    }
    
    // TODO: Doesn't work as intended.
//    private static <T> boolean hasDuplicate(List<T> all) {
//        Set<T> set = new HashSet<>();
//        for(T each : all) {
//            if(!set.add(each)) {
//                return true;
//            }
//        }
//        
//        return false;
//    }
}
