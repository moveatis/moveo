package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "categoryBean")
@SessionScoped
public class CategoryManagedBean implements Serializable {
    
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
        private String id;
        private String name;
        private List<String> categories;
        private String[] selectedCategories;
        
        public CategorySet(String id) {
            this.id = id;
        }
        
        public String getId() {
            return id;
        }
        
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
        CategorySet categorySet = new CategorySet("opettajan-toiminnot");
        categorySet.setName("Opettajan toiminnot");
        categorySet.setCategories(categories);
        categorySets.add(categorySet);
        categorySet = new CategorySet("oppilaan-toiminnot");
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
