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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Ilari Paananen <ilari.k.paananen at student.jyu.fi>
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
        // TODO: Does this method work in all situtations?
        // Getting ResourceBundle based on this: http://stackoverflow.com/a/9434554
        ResourceBundle messages = ResourceBundle.getBundle("com.moveatis.messages.Messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                  messages.getString("dialogErrorTitle"),
                                                  messages.getString("cs_errorNoneSelected")));
        return "";
    }
}
