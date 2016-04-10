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
package com.moveatis.lotas.managedbeans;

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
import com.moveatis.lotas.interfaces.MessageBundle;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Ilari Paananen <ilari.k.paananen at student.jyu.fi>
 */
@Named(value = "categorySelectionBean")
//@ManagedBean(name="categoryBean") //bean needs to be ManagedBean in order to ManagedProperty to work
@SessionScoped
public class CategorySelectionManagedBean implements Serializable {
    
    public class Category {
        private String name;
        private boolean selected;
        
        public Category(String name) {
            this.name = name;
            this.selected = true;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public boolean isSelected() {
            return selected;
        }
        
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
    
    public class CategorySet {
        private String name;
        private List<Category> categories;
        
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
        
        public void addCategory() {
            categories.add(new Category(""));
            LOGGER.debug("Added new category to set " + name);
        }
        
        public void removeCategory(Category category) {
            categories.remove(category);
            LOGGER.debug("Removed category from set " + name);
        }
        
        public List<String> getSelectedCategories() {
            List<String> selectedCategories = new ArrayList<>();
            for (Category category : categories) {
                if (category.isSelected()) {
                    String categoryName = category.getName();
                    if (categoryName.length() > 0 && selectedCategories.indexOf(categoryName) < 0) {
                        selectedCategories.add(categoryName);
                    }
                }
            }
            return selectedCategories;
        }
    }
    
    //
    //
    //
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategorySelectionManagedBean.class);
    private static final long serialVersionUID = 1L;
    
    private List<CategorySet> categorySets;
    
    /*
    * To Ilari:
    * The bean has to be "ManagedBean", eg annotated @ManagedBean, now its CDI bean since
    * its annotated as @Named.
    */

    @Inject @MessageBundle //created MessageBundle to allow resourcebundle injection to CDI beans
    private transient ResourceBundle messages;  //RequestBundle is not serializable (this bean is @SessionScoped) so it needs to be transient
    
    /**
     * Creates a new instance of CategoryManagedBean
     */
    public CategorySelectionManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        categorySets = new ArrayList<>();
        
        String[] opettajanToiminnot = {
            "Järjestelyt",
            "Tehtävän selitys",
            "Ohjaus",
            "Palautteen anto",
            "Tarkkailu",
            "Muu toiminta"
        };
        addCategorySet("Opettajan toiminnot", Arrays.asList(opettajanToiminnot));
        
        String[] oppilaanToiminnot = { "Oppilas suorittaa tehtävää" };
        addCategorySet("Oppilaan toiminnot", Arrays.asList(oppilaanToiminnot));
        
        addCategorySet("Muut", new ArrayList<String>());
    }
    
    public void addCategorySet(String name, List<String> categories) {
        CategorySet categorySet = new CategorySet();
        categorySet.setName(name);
        categorySet.setCategories(new ArrayList<Category>());
        for (String category : categories) {
            categorySet.add(category);
        }
        categorySets.add(categorySet);
    }
    
    public List<CategorySet> getCategorySets() {
        return categorySets;
    }
    
    public String checkSelectedCategories() {
        for (CategorySet categorySet : categorySets) {
            List<String> selectedCategories = categorySet.getSelectedCategories();
            if (!selectedCategories.isEmpty()) return "categoriesok";
        }
        
        // NOTE: Managed property for messages doesn't work.
        // FYI Ilari: Thats because this bean is not managedbean (eg @ManagedBean), its CDI bean (@Named)
        // TODO: Does this method work in all situtations?
        // Getting ResourceBundle based on this: http://stackoverflow.com/a/9434554
        // Not needed anymore as there is MessageBundle injected
        //ResourceBundle messages = ResourceBundle.getBundle("com.moveatis.messages.Messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                  messages.getString("dialogErrorTitle"),
                                                  messages.getString("cs_errorNoneSelected")));
        return "";
    }
}
