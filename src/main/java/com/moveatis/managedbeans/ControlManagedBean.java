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
import com.moveatis.category.CategoryType;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.interfaces.Category;
import com.moveatis.interfaces.CategorySet;
import com.moveatis.interfaces.Event;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Observation;
import com.moveatis.interfaces.Session;
import com.moveatis.label.LabelEntity;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.user.AbstractUser;
import com.moveatis.user.IdentifiedUserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@Named(value = "controlBean")
@ViewScoped
public class ControlManagedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ControlManagedBean.class);

    private List<EventGroupEntity> eventGroups;
    private List<CategoryEntity> categories;
    private List<ObservationEntity> observations;

    private EventGroupEntity selectedEventGroup;
    private CategorySetEntity selectedCategorySet;
    private CategoryEntity selectedCategory;
    private ObservationEntity selectedObservation;

    private boolean creatingNewEventGroup = false;

    @Inject
    private EventGroup eventGroupEJB;
    @Inject
    private Event eventEJB;
    @Inject
    private CategorySet categorySetEJB;
    @Inject
    private Category categoryEJB;
    @Inject
    private Observation observationEJB;
    @Inject
    private CategorySetManagedBean categorySetBean;

    @Inject
    private Session sessionBean;
    @Inject
    private ObservationManagedBean observationBean;

    @Inject
    @MessageBundle
    private transient ResourceBundle messages;

    private AbstractUser user;

    public ControlManagedBean() {
    }

    @PostConstruct
    public void init() {
        user = sessionBean.getLoggedIdentifiedUser();
        eventGroups = eventGroupEJB.findAllForOwner(user);
    }

    public Set<ObservationEntity> getObservations(EventGroupEntity eventGroup) {
        if (eventGroup != null && eventGroup.getEvent() != null) {
            return eventGroup.getEvent().getObservations();
        }
        return new TreeSet<>();
    }

    public boolean isCreatingNewEventGroup() {
        return creatingNewEventGroup;
    }

    public void setCreatingNewEventGroup(boolean creatingNewEventGroup) {
        this.creatingNewEventGroup = creatingNewEventGroup;
    }

    public boolean hasGroupKey(EventGroupEntity eventGroup) {
        return eventGroup != null && eventGroup.getGroupKey() != null;
    }

    public void addNewCategorySet() {
        selectedCategorySet = new CategorySetEntity();
        categories = new ArrayList<>();
    }

    public void addNewCategory() {
        CategoryEntity category = new CategoryEntity();
        LabelEntity label = new LabelEntity();
        category.setOrderNumber(categories.size());
        category.setLabel(label);
        
        List<CategoryEntity> labelCategoryEntities = label.getCategoryEntities();
        if(labelCategoryEntities == null) {
            labelCategoryEntities = new ArrayList<>();
        }
        labelCategoryEntities.add(category);
        label.setCategoryEntities(labelCategoryEntities);
        
        category.setCategoryType(CategoryType.TIMED);
        categories.add(category);
        selectedCategory = category;
    }

    public void onEditEventGroup(RowEditEvent event) {
        EventGroupEntity eventGroup = (EventGroupEntity) event.getObject();
        eventGroupEJB.edit(eventGroup);
    }

    public List<EventGroupEntity> getEventGroups() {
        return eventGroups;
    }

    public void setEventGroups(List<EventGroupEntity> eventGroups) {
        this.eventGroups = eventGroups;
    }

    public EventGroupEntity getSelectedEventGroup() {
        return selectedEventGroup;
    }

    public void setSelectedEventGroup(EventGroupEntity selectedEventGroup) {
        this.selectedEventGroup = selectedEventGroup;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public CategorySetEntity getSelectedCategorySet() {
        return selectedCategorySet;
    }

    public void setSelectedCategorySet(CategorySetEntity selectedCategorySet) {
        this.selectedCategorySet = selectedCategorySet;
        this.selectedEventGroup = this.selectedCategorySet.getEventGroupEntity();
        categories = new ArrayList<>(selectedCategorySet.getCategoryEntitys().values());
    }

    public CategoryEntity getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(CategoryEntity selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public ObservationEntity getSelectedObservation() {
        return selectedObservation;
    }

    public void setSelectedObservation(ObservationEntity selectedObservation) {
        this.selectedObservation = selectedObservation;
    }

    public CategoryType[] getCategoryTypes() {
        return CategoryType.values();
    }

    public String getObserverName() {
        if (selectedObservation == null) {
            return "";
        } else if (selectedObservation.getObserver() instanceof IdentifiedUserEntity) {
            return ((IdentifiedUserEntity) selectedObservation.getObserver()).getGivenName();
        } else {
            return "Julkinen käyttäjä";
        }
    }

    public String newObservation() {
        sessionBean.setEventEntityForNewObservation(selectedEventGroup.getEvent());
        return "newobservation";
    }

    public void removeEventGroup() {
        if (selectedEventGroup != null) {
            eventGroupEJB.remove(selectedEventGroup);
            eventGroups.remove(selectedEventGroup);
            selectedEventGroup = null;
        }
    }

    public void removeCategorySet() {
        if (selectedCategorySet != null) {
            categorySetEJB.remove(selectedCategorySet);
            selectedCategorySet = null;
            selectedCategory = null;
            // refetch eventgroups, maybe other way to update it?
            init();
        }
    }

    public void removeCategory() {
        if (selectedCategory != null) {
            int index = selectedCategory.getOrderNumber();
            categories.remove(index);
            int i = 0;
            for (CategoryEntity category : categories) {
                category.setOrderNumber(i);
                i++;
            }
            if (categories.isEmpty()) {
                selectedCategory = null;
            } else if (index > 0) {
                selectedCategory = categories.get(index - 1);
            } else {
                selectedCategory = categories.get(0);
            }
        }
    }

    public void removeObservation() {
        if (selectedObservation != null) {
            observationEJB.remove(selectedObservation);
            selectedObservation = null;
            this.init();
        }
    }

    public void onCategoryReorder(ReorderEvent event) {
        int i = 0;
        for (CategoryEntity category : categories) {
            category.setOrderNumber(i);
            i++;
        }
    }

    public void onEditObservation() {
        if (selectedObservation != null) {
            observationEJB.edit(selectedObservation);
        }
    }

    public void addEventGroup(EventGroupEntity eventGroup) {
        eventGroups.add(eventGroup);
    }

    public void saveCategorySet() {
        if (selectedEventGroup != null && selectedCategorySet != null) {
            categorySetBean.createNewCategorySet(selectedEventGroup, selectedCategorySet, categories);
            this.init();
        }
    }

    public String showObservationInSummaryPage() {
        observationBean.setObservationEntity(selectedObservation);
        observationBean.setCategorySetsInUse(new ArrayList<>(selectedObservation.getObservationCategorySets()));
        return "summary";
    }
}
