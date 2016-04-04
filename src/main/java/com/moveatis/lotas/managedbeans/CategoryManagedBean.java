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
