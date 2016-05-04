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
import java.util.ArrayList;
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
import com.moveatis.user.IdentifiedUserEntity;
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
    
    public static class Category {
        private long id;
        private long type;
        private String name;
        private boolean inDatabase;
        
        public Category() {
            this.id = 0l;
            this.type = 0l;
            this.name = "";
            this.inDatabase = false;
        }
        
        public Category(Long id, String name) {
            this.id = id;
            this.type = 0l;
            this.name = name;
            this.inDatabase = true;
        }
        
        public Category(Category other) {
            this.id = other.id;
            this.type = other.type;
            this.name = other.name;
            this.inDatabase = other.inDatabase;
            // NOTE: inDatabase should always be true when cloning other category!
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(long id) {
            this.id = id;
        }
        
        public Long getType() {
            return type;
        }
        
        public String getName() {
             return name;
        }
        
        public final void setName(String name) {
            String validName = Validation.validateForJsAndHtml(name).trim();
            if (!this.name.equals(validName)) {
                this.name = validName;
                // If the name is edited, it's not anymore in the database.
                this.inDatabase = false;
            }
        }
        
        public Boolean isInDatabase() {
            return inDatabase;
        }
        
        public void setInDatabase(boolean inDatabase) {
            this.inDatabase = inDatabase;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            Category category = (Category)o;
            return name.equals(category.name);
        }
    }
    
    public static class CategorySet {
        private Long id;
        private String name;
        private List<Category> categories;
        
        public CategorySet(Long id, String name) {
            this.id = id;
            this.name = name;
            this.categories = new ArrayList<>();
        }
        
        public Long getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        public List<Category> getCategories() {
            return categories;
        }
        
        public void add(Category category) {
            categories.add(category);
        }
        
        public void addEmpty() {
            categories.add(new Category());
        }
        
        public void remove(Category category) {
            categories.remove(category);
        }
    }
    
    public static class CategorySetList {
        private List<CategorySet> categorySets = new ArrayList<>();
        
        public List<CategorySet> getCategorySets() {
            return categorySets;
        }
        
        public void setCategorySets(List<CategorySet> categorySets) {
            this.categorySets = categorySets;
        }
        
        public void add(CategorySet categorySet) {
            categorySets.add(categorySet);
        }
        
        public void addClone(CategorySet categorySet) {
            CategorySet cloned = new CategorySet(categorySet.getId(), categorySet.getName());
            for (Category category : categorySet.getCategories()) {
                cloned.add(new Category(category));
            }
            categorySets.add(cloned);
        }
        
        public CategorySet find(Long id) {
            for (CategorySet categorySet : categorySets) {
                if (categorySet.getId().equals(id)) {
                    return categorySet;
                }
            }
            return null;
        }
        
        public void remove(CategorySet categorySet) {
            categorySets.remove(categorySet);
        }
    }
    

    private static final Logger LOGGER = LoggerFactory.getLogger(CategorySelectionManagedBean.class);
    private static final long serialVersionUID = 1L;
    
    private String newCategorySetName;
    
    private Long selectedDefaultCategorySet;
    private Long selectedPublicCategorySet;
    private Long selectedPrivateCategorySet;
    
    private CategorySetList defaultCategorySets; // From group key or event that was selected in control page.
    private CategorySetList publicCategorySets;
    private CategorySetList privateCategorySets;
    private CategorySetList categorySetsInUse;
    
    private EventGroupEntity eventGroup;
    
    @Inject
    private Session sessionBean;
    
    @Inject
    private EventGroup eventGroupEJB;

    @Inject @MessageBundle //created MessageBundle to allow resourcebundle injection to CDI beans
    private transient ResourceBundle messages;  //RequestBundle is not serializable 
    
    /**
     * Creates a new instance of CategoryManagedBean
     */
    public CategorySelectionManagedBean() {
    }
    
    private static void addAllCategorySetsFromEventGroup(CategorySetList addTo, EventGroupEntity eventGroup) {
        Set<CategorySetEntity> categorySets = eventGroup.getCategorySets();
        for (CategorySetEntity categorySetEntity : categorySets) {
            CategorySet categorySet = new CategorySet(categorySetEntity.getId(), categorySetEntity.getLabel());
            Map<Integer, CategoryEntity> categories = categorySetEntity.getCategoryEntitys();
            for (CategoryEntity category : categories.values()) {
                categorySet.add(new Category(category.getId(), category.getLabel().getLabel()));
            }
            addTo.add(categorySet);
        }
    }
    
    private static CategorySetList getAllCategorySetsFromEventGroup(EventGroupEntity eventGroup) {
        CategorySetList categorySets = new CategorySetList();
        addAllCategorySetsFromEventGroup(categorySets, eventGroup);
        if (categorySets.getCategorySets().isEmpty())
            return null;
        return categorySets;
    }
    
    private static CategorySetList getAllCategorySetsFromEventGroups(List<EventGroupEntity> eventGroups) {
        CategorySetList categorySets = new CategorySetList();
        for (EventGroupEntity eventGroup : eventGroups) {
            addAllCategorySetsFromEventGroup(categorySets, eventGroup);
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
        
        categorySetsInUse = new CategorySetList();
        List<CategorySet> categorySets = sessionBean.getCategorySetsInUse();
        if (categorySets != null) {
            categorySetsInUse.setCategorySets(categorySets);
        } else if (defaultCategorySets != null) {
            for(CategorySet categorySet : defaultCategorySets.getCategorySets()) {
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
    
    public List<CategorySet> getDefaultCategorySets() {
        return defaultCategorySets.getCategorySets();
    }
    
    public List<CategorySet> getPublicCategorySets() {
        return publicCategorySets.getCategorySets();
    }
    
    public List<CategorySet> getPrivateCategorySets() {
        return privateCategorySets.getCategorySets();
    }
    
    public List<CategorySet> getCategorySetsInUse() {
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
        if (!newCategorySetName.isEmpty()) {
            CategorySet categorySet = new CategorySet(-1l, newCategorySetName); // TODO: id?
            categorySetsInUse.add(categorySet);
            newCategorySetName = "";
        }
    }
    
    public void addDefaultCategorySet() {
        if (categorySetsInUse.find(selectedDefaultCategorySet) == null) {
            CategorySet categorySet = defaultCategorySets.find(selectedDefaultCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected default category set not found!");
        }
    }
    
    public void addPublicCategorySet() {
        if (categorySetsInUse.find(selectedPublicCategorySet) == null) {
            CategorySet categorySet = publicCategorySets.find(selectedPublicCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected public category set not found!");
        }
    }
    
    public void addPrivateCategorySet() {
        if (categorySetsInUse.find(selectedPrivateCategorySet) == null) {
            CategorySet categorySet = privateCategorySets.find(selectedPrivateCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected private category set not found!");
        }
    }
    
    public void removeCategorySet(CategorySet categorySet) {
        categorySetsInUse.remove(categorySet);
    }
    
    private void showErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), message));
    }
    
    public String checkCategories() {
        boolean atLeastOneCategorySelected = false;
        
        List<Category> notInDatabase = new ArrayList<>();
        long greatestId = 0;
        
        for (CategorySet categorySet : categorySetsInUse.getCategorySets()) {
            
            List<Category> categories = categorySet.getCategories();
            if (!categories.isEmpty()) {
                atLeastOneCategorySelected = true;
            }
            
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                
                if (category.getName().isEmpty()) {
                    showErrorMessage(messages.getString("cs_warningEmptyCategories"));
                    return ""; // TODO: Show confirmation or something and let user continue.
                }
                
                if (categories.lastIndexOf(category) != i) {
                    showErrorMessage(messages.getString("cs_errorNotUniqueCategories"));
                    return "";
                }
                
                if (category.isInDatabase()) {
                    long id = category.getId();
                    if (id > greatestId) {
                        greatestId = id;
                    }
                } else {
                    notInDatabase.add(category);
                }
            }
        }
        
        if (!atLeastOneCategorySelected) {
            showErrorMessage(messages.getString("cs_errorNoneSelected"));
            return "";
        }
        
        // Make category ids unique in this observation.
        for (Category category : notInDatabase) {
            category.setId(greatestId + 1);
            greatestId += 1;
        }
        
        sessionBean.setCategorySetsInUse(categorySetsInUse.getCategorySets());
        return "categoriesok";
    }
}
