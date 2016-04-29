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
import com.moveatis.event.EventGroupEntity;
import com.moveatis.interfaces.EventGroup;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.moveatis.user.AbstractUser;
import java.util.Set;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Ilari Paananen <ilari.k.paananen at student.jyu.fi>
 */
@Named(value = "categorySelectionBean")
@SessionScoped // TODO: ViewScoped? RequestScoped? Used in index.xhtml and also needs to run init() every time page is loaded!
public class CategorySelectionManagedBean implements Serializable {
    
    public static class Category {
        private long id;
        private long type;
        private String name;
        private boolean selected;
        
        private static long nextId = 1; // Can ids start from 0?
        
        public Category(String name) {
            id = nextId++;
            type = 0;
            setName(name);
            selected = true;
        }
        
        public long getId() {
            return id;
        }
        
        public long getType() {
            return type;
        }
        
        public String getName() {
             return name;
        }
        
        public final void setName(String name) {
            StringBuilder validName = new StringBuilder();
            for (int i = 0; i < name.length(); ) {
                int codePoint = name.codePointAt(i);
                if (Character.isLetterOrDigit(codePoint)) {
                    validName.appendCodePoint(codePoint);
                } else if (Character.isSpaceChar(codePoint)) {
                    validName.append(' ');
                }
                i += Character.charCount(codePoint);
            }
            this.name = validName.toString().trim();
        }
        
        public boolean isSelected() {
            return selected;
        }
        
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            Category category = (Category)o;
            return name.equals(category.name);
        }
    }
    
    public static class CategorySet {
        private String name;
        private List<Category> categories = new ArrayList<>();
        private List<Category> userCategories = new ArrayList<>();
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public List<Category> getCategories() {
            return categories;
        }
        
        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }
        
        public void add(String category) {
            categories.add(new Category(category));
        }
        
        public List<Category> getUserCategories() {
            return userCategories;
        }
        
        public void addEmpty() {
            userCategories.add(new Category(""));
        }
        
        public void remove(Category category) {
            userCategories.remove(category);
        }
        
        public boolean allCategoriesHaveUniqueName() {
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                if (category.getName().isEmpty()) {
                    continue;
                }
                if (categories.lastIndexOf(category) != i) {
                    return false;
                }
                if (userCategories.lastIndexOf(category) >= 0) {
                    return false;
                }
            }
            for (int i = 0; i < userCategories.size(); i++) {
                Category category = userCategories.get(i);
                if (category.getName().isEmpty()) {
                    continue;
                }
                if (userCategories.lastIndexOf(category) != i) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean allCategoriesHaveNonEmptyName() {
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                if (category.getName().isEmpty()) {
                    return false;
                }
            }
            for (int i = 0; i < userCategories.size(); i++) {
                Category category = userCategories.get(i);
                if (category.getName().isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean atLeastOneCategorySelected() {
            if (!userCategories.isEmpty()) {                
                return true;
            }
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                if (category.isSelected()) {
                    return true;
                }
            }
            return false;
        }
        
//        public List<String> getSelectedCategories() {
//            List<String> selectedCategories = new ArrayList<>();
//            for (Category category : categories) {
//                if (category.isSelected()) {
//                    String categoryName = category.getName();
//                    // TODO: Do we have to check name length and duplicates?
//                    if (categoryName.length() > 0 && selectedCategories.indexOf(categoryName) < 0) {
//                        selectedCategories.add(categoryName);
//                    }
//                }
//            }
//            for (Category category : userCategories) {
//                if (category.isSelected()) {
//                    String categoryName = category.getName();
//                    if (categoryName.length() > 0 && selectedCategories.indexOf(categoryName) < 0) {
//                        selectedCategories.add(categoryName);
//                    }
//                }
//            }
//            return selectedCategories;
//        }
        
        public List<Category> getSelectedCategories() {
            List<Category> selectedCategories = new ArrayList<>();
            for (Category category : categories) {
                if (category.isSelected()) {
                    selectedCategories.add(category);
                }
            }
            for (Category category : userCategories) {
                selectedCategories.add(category);
            }
            return selectedCategories;
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
        
        public void add(String name, List<String> categories) {
            CategorySet categorySet = new CategorySet();
            categorySet.setName(name);
            for (String category : categories) {
                categorySet.add(category);
            }
            categorySets.add(categorySet);
        }
        
        public void addClone(CategorySet categorySet) {
            CategorySet cloned = new CategorySet();
            cloned.setName(categorySet.getName());
            for (Category category : categorySet.getCategories()) {
                cloned.add(category.getName());
            }
            categorySets.add(cloned);
        }
        
        // TODO: Find by id? index? what?
        public CategorySet find(String name) {
            for (CategorySet categorySet : categorySets) {
                if (categorySet.getName().equals(name)) {
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
    
    private String selectedPublicCategorySet;
    private String selectedPrivateCategorySet;
    
    private CategorySetList publicCategorySets;
    private CategorySetList privateCategorySets;
    private CategorySetList categorySetsInUse;
    
    @Inject
    private Session sessionBean;
    
    @Inject
    private EventGroup eventGroupEJB;
    
    @Inject
    private com.moveatis.interfaces.CategorySet categorySetEJB;
    
    @Inject
    private com.moveatis.interfaces.Category categoryEJB;

    @Inject @MessageBundle //created MessageBundle to allow resourcebundle injection to CDI beans
    private transient ResourceBundle messages;  //RequestBundle is not serializable (this bean is @SessionScoped) so it needs to be transient
    
    /**
     * Creates a new instance of CategoryManagedBean
     */
    public CategorySelectionManagedBean() {
    }
    
    @PostConstruct
    public void init() {
//        publicCategorySets = new CategorySetList();
//
//        AbstractUser user = sessionBean.getLoggedInUser();
//        List<EventGroupEntity> publicEventGroups = eventGroupEJB.findAllForOwner(user);
//        for (EventGroupEntity eventGroup : publicEventGroups) {
//            Set<CategorySetEntity> categorySets = eventGroup.getCategoryGroups();
//            for (CategorySetEntity categorySet : categorySets) {
//                List<String> categoryNames = new ArrayList<>();
//                List<CategoryEntity> categories = categorySet.getCategoryEntitys();
//                for (CategoryEntity category : categories) {
//                    categoryNames.add(category.getLabel().getLabel());
//                }
//                publicCategorySets.add(categorySet.getLabel(), categoryNames);
//            }
//        }
        
        publicCategorySets = new CategorySetList();
        String[] opettajanToiminnot = {
            "Järjestelyt",
            "Tehtävän selitys",
            "Ohjaus",
            "Palautteen anto",
            "Tarkkailu",
            "Muu toiminta"
        };
        publicCategorySets.add("Opettajan toiminnot", Arrays.asList(opettajanToiminnot));
        String[] oppilaanToiminnot = { "Oppilas suorittaa tehtävää" };
        publicCategorySets.add("Oppilaan toiminnot", Arrays.asList(oppilaanToiminnot));
        
        privateCategorySets = new CategorySetList();
        String[] omatToiminnot1 = { "Tekee jotain 1", "Tekee jotain 2", "Tekee jotain 3" };
        privateCategorySets.add("Omat toiminnot 1", Arrays.asList(omatToiminnot1));
        String[] omatToiminnot2 = { "Tekee jotain muuta 1", "Tekee jotain muuta 2", "Tekee jotain muuta 3" };
        privateCategorySets.add("Omat toiminnot 2", Arrays.asList(omatToiminnot2));
        privateCategorySets.add("Muut", new ArrayList<String>());
        
        categorySetsInUse = new CategorySetList();
        for(CategorySet categorySet : publicCategorySets.getCategorySets()) {
            categorySetsInUse.addClone(categorySet);
        }
        categorySetsInUse.add("Muut", new ArrayList<String>());
    }
    
    public String getSelectedPublicCategorySet() {
        return selectedPublicCategorySet;
    }
    
    public void setSelectedPublicCategorySet(String selectedPublicCategorySet) {
        this.selectedPublicCategorySet = selectedPublicCategorySet;
    }
    
    public String getSelectedPrivateCategorySet() {
        return selectedPrivateCategorySet;
    }
    
    public void setSelectedPrivateCategorySet(String selectedPrivateCategorySet) {
        this.selectedPrivateCategorySet = selectedPrivateCategorySet;
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
    
    private void addErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), message));
    }
    
    private void addWarningMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, messages.getString("dialogWarningTitle"), message));
    }
    
    public String checkCategories() {
        boolean atLeastOneCategorySelected = false;
        
        for (CategorySet categorySet : categorySetsInUse.getCategorySets()) {
            if (!categorySet.allCategoriesHaveUniqueName()) {
                addErrorMessage(messages.getString("cs_errorNotUniqueCategories"));
                return "";
            }
            if (!categorySet.allCategoriesHaveNonEmptyName()) {
                addWarningMessage(messages.getString("cs_warningEmptyCategories"));
                return ""; // TODO: Show confirmation or something and let user continue.
            }
            if (categorySet.atLeastOneCategorySelected()) {
                atLeastOneCategorySelected = true;
            }
        }
        
        if (!atLeastOneCategorySelected) {
            addErrorMessage(messages.getString("cs_errorNoneSelected"));
            return "";
        }
        
        return "categoriesok";
    }
}