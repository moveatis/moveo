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
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "categoryBean")
@SessionScoped
public class CategoryManagedBean implements Serializable {
    
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
        
        public void addCategory() {
            categories.add(new Category(""));
            LOGGER.debug("Added new category to set " + name);
        }
        
        public void removeCategory(Category category) {
            categories.remove(category);
            LOGGER.debug("Removed category from set " + name);
        }
        
        public String[] getSelectedCategories() {
            List<String> selectedCategories = new ArrayList<>();
            for (Category category : categories) {
                if (category.isSelected()) {
                    String categoryName = category.getName();
                    if (categoryName.length() > 0 && selectedCategories.indexOf(categoryName) < 0) {
                        selectedCategories.add(categoryName);
                    }
                }
            }
            
            String[] categoryArr = new String[selectedCategories.size()];
            return selectedCategories.toArray(categoryArr);
        }
    }
    
    //
    //
    //
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryManagedBean.class);
    private static final long serialVersionUID = 1L;
    
    private List<CategorySet> categorySets;

    /**
     * Creates a new instance of CategoryManagedBean
     */
    public CategoryManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        categorySets = new ArrayList<>();
        
        CategorySet categorySet = new CategorySet();
        categorySet.setName("Opettajan toiminnot");
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Järjestelyt"));
        categories.add(new Category("Tehtävän selitys"));
        categories.add(new Category("Ohjaus"));
        categories.add(new Category("Palautteen anto"));
        categories.add(new Category("Tarkkailu"));
        categories.add(new Category("Muu toiminta"));
        categorySet.setCategories(categories);
        categorySets.add(categorySet);
        
        categorySet = new CategorySet();
        categorySet.setName("Oppilaan toiminnot");
        categories = new ArrayList<>();
        categories.add(new Category("Oppilas suorittaa tehtävää"));
        categorySet.setCategories(categories);
        categorySets.add(categorySet);
        
        categorySet = new CategorySet();
        categorySet.setName("Muut");
        categories = new ArrayList<>();
        categorySet.setCategories(categories);
        categorySets.add(categorySet);
    }
    
    public List<CategorySet> getCategorySets() {
        return categorySets;
    }
}
