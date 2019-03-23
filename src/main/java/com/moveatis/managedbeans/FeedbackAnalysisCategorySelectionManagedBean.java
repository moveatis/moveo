package com.moveatis.managedbeans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.abstracts.AbstractCategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.helpers.Validation;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Session;
import com.moveatis.label.LabelEntity;
import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationCategorySetList;
import com.moveatis.user.IdentifiedUserEntity;

@Named(value = "feedbackAnalysisCategorySelectionManagedBean")
@ViewScoped
public class FeedbackAnalysisCategorySelectionManagedBean implements Serializable{
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackAnalysisCategorySelectionManagedBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String newCategorySetName;
    
    @Inject @MessageBundle //created MessageBundle to allow resourcebundle injection to CDI beans
    private transient ResourceBundle messages;  //RequestBundle is not serializable 
    
    private long selectedDefaultCategorySet;
    private long selectedPrivateCategorySet;
    
    
    
    private Set<FeedbackAnalysisCategorySetEntity> defaultCategorySets; // From group key or event that was selected in control page.
    private Set<FeedbackAnalysisCategorySetEntity>  privateCategorySets;
    private Set<FeedbackAnalysisCategorySetEntity>  categorySetsInUse;
    
    private EventGroupEntity eventGroup;
    
    @Inject
    private Session sessionEJB;
    @Inject
    private EventGroup eventGroupEJB;
	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;
	
	@PostConstruct
	public void init() {
		eventGroup = null;
		defaultCategorySets=new HashSet<FeedbackAnalysisCategorySetEntity>(); 
        privateCategorySets=new HashSet<FeedbackAnalysisCategorySetEntity>();
        categorySetsInUse=new HashSet<FeedbackAnalysisCategorySetEntity>();  
        if (feedbackAnalyzationManagedBean.getEventEntity() != null) {
            EventEntity event = feedbackAnalyzationManagedBean.getEventEntity();
            eventGroup = event.getEventGroup();
            defaultCategorySets=eventGroup.getFeedbackAnalysisCategorySets();
        }

        if (sessionEJB.isIdentifiedUser()) {
            IdentifiedUserEntity user = sessionEJB.getLoggedIdentifiedUser();
            for (EventGroupEntity eg: eventGroupEJB.findAllForOwner(user))
            	for (FeedbackAnalysisCategorySetEntity fba:eg.getFeedbackAnalysisCategorySets())
            		privateCategorySets.add(fba);
        }
        
        Set<FeedbackAnalysisCategorySetEntity> categorySets = sessionEJB.getFeedbackAnalysisCategorySetsInUse();
        if (categorySets != null) {
            categorySetsInUse=categorySets;
        } else {
            for(FeedbackAnalysisCategorySetEntity categorySet : defaultCategorySets) {
                categorySetsInUse.add(categorySet);
            }
        }
    }
	
	 /**
     * Gets the new category set name.
     */
    public String getNewCategorySetName() {
        return newCategorySetName;
    }
    
    /**
     * Sets the new category set name.
     */
    public void setNewCategorySetName(String newCategorySetName) {
        this.newCategorySetName = newCategorySetName;
    }
    
    /**
     * Gets the selected default category set.
     */
    public long getSelectedDefaultCategorySet() {
        return selectedDefaultCategorySet;
    }
    
    /**
     * Sets the selected default category set.
     */
    public void setSelectedDefaultCategorySet(long selectedDefaultCategorySet) {
        this.selectedDefaultCategorySet = selectedDefaultCategorySet;
    }
    
    /**
     * Gets the selected private category set.
     */
    public long getSelectedPrivateCategorySet() {
        return selectedPrivateCategorySet;
    }
    
    
    /**
     * Sets the selected private category set.
     */
    public void setSelectedPrivateCategorySet(long selectedPrivateCategorySet) {
        this.selectedPrivateCategorySet = selectedPrivateCategorySet;
    }
    
    /**
     * Gets the default category sets.
     */
    public Set<FeedbackAnalysisCategorySetEntity> getDefaultCategorySets() {
        return defaultCategorySets;
    }
    
    /**
     * Gets the private category sets.
     */
    public Set<FeedbackAnalysisCategorySetEntity> getPrivateCategorySets() {
        return privateCategorySets;
    }
    
    /**
     * Gets the category sets in use.
     */
    public Set<FeedbackAnalysisCategorySetEntity> getCategorySetsInUse() {
        return categorySetsInUse;
    }
    
    /**
     * Gets the event group.
     */
    public EventGroupEntity getEventGroup() {
        return eventGroup;
    }
    
    /**
     * Adds a new category set for the observation if newCategorySetName isn't empty.
     */
    public void addNewCategorySet() {
        String name = Validation.validateForJsAndHtml(newCategorySetName);
        
        if (!name.isEmpty()) {
            // TODO: Is this wanted? What about default category sets?
            // Can they be added if there is already category with same name in use?
            for (FeedbackAnalysisCategorySetEntity set : categorySetsInUse) {
                if (name.equals(set.getLabel())) {
                    showErrorMessage(messages.getString("cs_errorNotUniqueCategorySet"));
                    return;
                }
            }
            
            FeedbackAnalysisCategorySetEntity categorySet = new FeedbackAnalysisCategorySetEntity();
            categorySet.setLabel(name);
            Map<Integer, AbstractCategoryEntity> newCategoryEntities=new TreeMap<Integer,AbstractCategoryEntity>();
            categorySet.setCategoryEntitys(newCategoryEntities);
            categorySetsInUse.add(categorySet);
            newCategorySetName = "";
        }
    }
    public void addNewCategoryToCategorySet(FeedbackAnalysisCategorySetEntity categorySet) {
    	FeedbackAnalysisCategoryEntity fac=new FeedbackAnalysisCategoryEntity();
    	fac.setLabel(new LabelEntity());
    	
    	Map<Integer, AbstractCategoryEntity> categories=categorySet.getCategoryEntitys();
    	fac.setOrderNumber(categories.keySet().size());
    	categories.put(categories.keySet().size(),fac);
    }

    public void removeCategoryFromCategorySet(FeedbackAnalysisCategorySetEntity categorySet, FeedbackAnalysisCategoryEntity category) {
    	Map<Integer, AbstractCategoryEntity> categories=categorySet.getCategoryEntitys();
    	Map<Integer, AbstractCategoryEntity> tmp_categories=categorySet.getCategoryEntitys();
    	categories.remove(category.getOrderNumber());
    	int i=0;
    	
    	for(int key : categories.keySet()) {
    		tmp_categories.put(key,categories.get(key));
    	}
    	categorySet.setCategoryEntitys(tmp_categories);
    }
    
    private FeedbackAnalysisCategorySetEntity findById(Set<FeedbackAnalysisCategorySetEntity> set,long id) {
    	for (FeedbackAnalysisCategorySetEntity pcs : privateCategorySets){
    		if(pcs.getId()==selectedPrivateCategorySet) {
    				return(pcs);
    		}
    	}
    	return null;    	
    }
       /**
     * Adds the selected default category set for the observation.
     */
    public void addDefaultCategorySet() {
    	FeedbackAnalysisCategorySetEntity sdc=findById(defaultCategorySets,selectedDefaultCategorySet);
        if (!categorySetsInUse.contains(sdc)) {
            categorySetsInUse.add(sdc);
        }
    }
     
    /**
     * Adds the selected private category set for the observation.
     */
    public void addPrivateCategorySet() {
    	FeedbackAnalysisCategorySetEntity spc=findById(privateCategorySets,selectedPrivateCategorySet);
        if (!categorySetsInUse.contains(spc)) {
            categorySetsInUse.add(spc);
        }
    }
    
    /**
     * Removes the given category set from the observation.
     */
    public void removeCategorySet(FeedbackAnalysisCategorySetEntity categorySet) {
        categorySetsInUse.remove(categorySet);
    }
    
    /**
     * Checks if the continue button should be disabled.
     * The button is disabled if no category sets have been added for the observation
     * or if some of the added category sets are empty.
     * @return True if the continue button should be disabled.
     */
    public boolean isContinueDisabled() {
        for (FeedbackAnalysisCategorySetEntity categorySet : categorySetsInUse) {
            if (categorySet.getCategoryEntitys().isEmpty()) return true;
        }
        return categorySetsInUse.isEmpty();
    }
    
    /**
     * Shows given error message in primefaces message popup.
     * @param message Error message to show.
     */
    private void showErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), message));
    }
    
    /**
     * Checks the categories in use before letting the user continue to observation.
     * The categories in the same category set should have different names.
     * The categories shouldn't have empty names.
     * At least one category should be selected for the observation.
     * It shows an error message if the categories aren't ok.
     * @return "categoriesok" if the categories were ok, otherwise an empty string.
     */
    public String checkCategories() {
        boolean atLeastOneCategorySelected = false;
        
        for (FeedbackAnalysisCategorySetEntity categorySet : categorySetsInUse) {
            
            Map<Integer, AbstractCategoryEntity> categories = categorySet.getCategoryEntitys();
            
            if(hasDuplicate(categories)) {
                showErrorMessage(messages.getString("cs_errorNotUniqueCategories"));
                return "";
            }
            
            if (!categories.isEmpty()) {
                atLeastOneCategorySelected = true;
            } else {
                showErrorMessage(messages.getString("cs_warningEmptyCategorySets"));
                return ""; // TODO: Show confirmation or something and let user continue.
            }
            
            for (AbstractCategoryEntity category : categories.values()) {
                
                if (category.getLabel().getText().isEmpty()) {
                    showErrorMessage(messages.getString("cs_warningEmptyCategories"));
                    return ""; // TODO: Show confirmation or something and let user continue.
                }
            }
        }
        
        if (!atLeastOneCategorySelected) {
            showErrorMessage(messages.getString("cs_errorNoneSelected"));
            return "";
        }

        feedbackAnalyzationManagedBean.setCategorySetsInUse(categorySetsInUse);
        return "categoriesok";
    }
    
    /**
     * Checks if given categories contain duplicate names.
     * @param categories List of categories to check.
     * @return True if categories contain duplicates, otherwise false.
     */
    private static boolean hasDuplicate(Map<Integer, AbstractCategoryEntity> categories) {
        Set<String> set = new HashSet<>();
        for (AbstractCategoryEntity category : categories.values()) {
            String name = category.getLabel().getText();
            if (!name.isEmpty() && !set.add(name)) {
                return true;
            }
        }
        return false;
    }
	}

