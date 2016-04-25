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

import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.interfaces.Category;
import com.moveatis.interfaces.CategorySet;
import com.moveatis.interfaces.Event;
import com.moveatis.interfaces.EventGroup;
import javax.inject.Named;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Session;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "alternativeCategorySelectionBean")
@ViewScoped 
public class AlternativeCategorySelectionManagedBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AlternativeCategorySelectionManagedBean.class);
    private static final long serialVersionUID = 1L;

    @Inject
    private Session sessionBean;
    
    @Inject
    private EventGroup eventGroupEJB;
    
    @Inject
    private Event eventEJB;
    
    @Inject
    private CategorySet categorySetEJB;
    
    @Inject
    private Category categoryEJB;

    @Inject @MessageBundle 
    private transient ResourceBundle messages;  
    
    private Long selectedEventCategorySetId;
    private EventEntity selectedEvent;
    private Map<String, Long> eventGroupCategorySetsMap;
    private Map<Long, Boolean> selectedCategories;
    
    private Set<CategorySetEntity> categorySetsInUse;
    
    /**
     * Creates a new instance of AlternativeCategoryManagedBean
     */
    public AlternativeCategorySelectionManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        selectedEvent = sessionBean.getEventEntityForNewObservation();
        Set<CategorySetEntity> eventGroupCategorySets = selectedEvent.getEventGroup().getCategorySets();
        eventGroupCategorySetsMap = new TreeMap<>();
        for(CategorySetEntity categorySetEntity : eventGroupCategorySets) {
            eventGroupCategorySetsMap.put(categorySetEntity.getLabel(), categorySetEntity.getId());
        }
    }

    public Long getSelectedEventCategorySetId() {
        return selectedEventCategorySetId;
    }

    public void setSelectedEventCategorySetId(Long selectedEventCategorySetId) {
        this.selectedEventCategorySetId = selectedEventCategorySetId;
        LOGGER.debug("selectedEventCategorySetId -> " + selectedEventCategorySetId);
    }

    public Map<String, Long> getEventGroupCategorySetsMap() {
        return eventGroupCategorySetsMap;
    }

    public void setEventGroupCategorySetsMap(Map<String, Long> eventGroupCategorySetsMap) {
        this.eventGroupCategorySetsMap = eventGroupCategorySetsMap;
    }

    public List<CategorySetEntity> getCategorySetsInUse() {
        if(categorySetsInUse == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(categorySetsInUse);
        }
    }

    public void setCategorySetsInUse(Set<CategorySetEntity> categorySetsInUse) {
        this.categorySetsInUse = categorySetsInUse;
    }

    public Map<Long, Boolean> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(Map<Long, Boolean> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }
    
    private void addErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), message));
    }
    
    private void addWarningMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, messages.getString("dialogWarningTitle"), message));
    }
    
    public void addCategorySet() {
        if(categorySetsInUse == null) {
            categorySetsInUse = new HashSet<>();
        }
        categorySetsInUse.add(categorySetEJB.find(selectedEventCategorySetId));
        LOGGER.debug("Added -> " + selectedEventCategorySetId + ", set size -> " + categorySetsInUse.size());
    }
    
    public String ready() {
        return "success";
    }
    
    public void removeCategorySet() {
        
    }
}
