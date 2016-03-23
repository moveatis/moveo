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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryManagedBean.class);
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of CategoryManagedBean
     */
    public CategoryManagedBean() {
    }
    
    //
    //
    //
    
    public class CategorySet {
        private String name;
        private List<String> categories;
        private String[] selectedCategories;
        private String category = "";
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public List<String> getCategories() {
            return categories;
        }
        
        public void setCategories(List<String> categories) {
            this.categories = categories;
        }
        
        public String[] getSelectedCategories() {
            return selectedCategories;
        }
        
        public void setSelectedCategories(String[] selectedCategories) {
            this.selectedCategories = selectedCategories;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public void addCategory() {
            if (category.length() > 0) {
                if (categories.indexOf(category) < 0) {
                    LOGGER.debug("Added category " + category);
                    categories.add(category);
                    category = "";
                } else {
                    // What to do?
                }
            }
        }
    }
    
    private List<CategorySet> categorySets;
    
    private List<String> categories;
    private String[] selectedCategories;
    
    @PostConstruct
    public void init() {
        categories = new ArrayList<>();
        categories.add("Järjestelyt");
        categories.add("Tehtävän selitys");
        categories.add("Ohjaus");
        categories.add("Palautteen anto");
        categories.add("Tarkkailu");
        categories.add("Muu toiminta");

        List<String> oppilaanToiminnot = new ArrayList<>();
        oppilaanToiminnot.add("Oppilas suorittaa tehtävää");
        
        categorySets = new ArrayList<>();
        CategorySet categorySet = new CategorySet();
        categorySet.setName("Opettajan toiminnot");
        categorySet.setCategories(categories);
        categorySets.add(categorySet);
        categorySet = new CategorySet();
        categorySet.setName("Oppilaan toiminnot");
        categorySet.setCategories(oppilaanToiminnot);
        categorySets.add(categorySet);
    }
    
    public List<String> getCategories() {
        return categories;
    }
    
    public String[] getSelectedCategories() {
        return selectedCategories;
    }
    
    public void setSelectedCategories(String[] selectedCategories) {
        this.selectedCategories = selectedCategories;
    }
    
    public List<CategorySet> getCategorySets() {
        return categorySets;
    }
}
