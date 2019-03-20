package com.moveatis.managedbeans;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.helpers.Validation;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Session;
import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationCategorySetList;
import com.moveatis.user.IdentifiedUserEntity;

public abstract class AbstractCategorySelectionManagedBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategorySelectionManagedBean.class);

	
	private String newCategorySetName;
	
	private Long selectedDefaultCategorySet;
	private Long selectedPrivateCategorySet;
	
	protected ObservationCategorySetList defaultCategorySets; // From group key or event that was selected in control page.
	protected ObservationCategorySetList privateCategorySets;
	protected ObservationCategorySetList categorySetsInUse;
	
	
	protected EventGroupEntity eventGroup;
	
	@Inject
	protected Session sessionBean;
	@Inject
	protected EventGroup eventGroupEJB;

	// TODO: Messages aren't updated to match language selection. Get ResourceBundle some other way?
	@Inject @MessageBundle //created MessageBundle to allow resourcebundle injection to CDI beans
	protected
	transient ResourceBundle messages;  //RequestBundle is not serializable 
	
	private Long addedCategorySetTag = 0L;
	
	protected abstract void addAllCategorySetsFromEventGroup(ObservationCategorySetList addTo, EventGroupEntity eventGroup);   
	protected abstract void addAllCategorySetsFromEventGroups(ObservationCategorySetList categorySets, List<EventGroupEntity> eventGroups);
	    
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
    public Long getSelectedDefaultCategorySet() {
        return selectedDefaultCategorySet;
    }
    
    /**
     * Sets the selected default category set.
     */
    public void setSelectedDefaultCategorySet(Long selectedDefaultCategorySet) {
        this.selectedDefaultCategorySet = selectedDefaultCategorySet;
    }
    
    /**
     * Gets the selected private category set.
     */
    public Long getSelectedPrivateCategorySet() {
        return selectedPrivateCategorySet;
    }
    
    
    /**
     * Sets the selected private category set.
     */
    public void setSelectedPrivateCategorySet(Long selectedPrivateCategorySet) {
        this.selectedPrivateCategorySet = selectedPrivateCategorySet;
    }
    
    /**
     * Gets the default category sets.
     */
    public List<ObservationCategorySet> getDefaultCategorySets() {
        return defaultCategorySets.getCategorySets();
    }
    
    /**
     * Gets the private category sets.
     */
    public List<ObservationCategorySet> getPrivateCategorySets() {
        return privateCategorySets.getCategorySets();
    }
    
    /**
     * Gets the category sets in use.
     */
    public List<ObservationCategorySet> getCategorySetsInUse() {
        return categorySetsInUse.getCategorySets();
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
            for (ObservationCategorySet set : categorySetsInUse.getCategorySets()) {
                if (name.equals(set.getName())) {
                    showErrorMessage(messages.getString("cs_errorNotUniqueCategorySet"));
                    return;
                }
            }
            
            ObservationCategorySet categorySet = new ObservationCategorySet(addedCategorySetTag++, name);
            categorySetsInUse.add(categorySet);
            newCategorySetName = "";
        }
    }
    
    /**
     * Adds the selected default category set for the observation.
     */
    public void addDefaultCategorySet() {
        if (categorySetsInUse.find(selectedDefaultCategorySet) == null) {
            ObservationCategorySet categorySet = defaultCategorySets.find(selectedDefaultCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected default category set not found!");
        }
    }
    
    /**
     * Adds the selected private category set for the observation.
     */
    public void addPrivateCategorySet() {
        if (categorySetsInUse.find(selectedPrivateCategorySet) == null) {
            ObservationCategorySet categorySet = privateCategorySets.find(selectedPrivateCategorySet);
            if (categorySet != null) categorySetsInUse.addClone(categorySet);
            else LOGGER.debug("Selected private category set not found!");
        }
    }
    
    /**
     * Removes the given category set from the observation.
     */
    public void removeCategorySet(ObservationCategorySet categorySet) {
        categorySetsInUse.remove(categorySet);
    }
    
    /**
     * Checks if the continue button should be disabled.
     * The button is disabled if no category sets have been added for the observation
     * or if some of the added category sets are empty.
     * @return True if the continue button should be disabled.
     */
    public boolean isContinueDisabled() {
        for (ObservationCategorySet categorySet : categorySetsInUse.getCategorySets()) {
            if (categorySet.getCategories().isEmpty()) return true;
        }
        return categorySetsInUse.getCategorySets().isEmpty();
    }
    
    /**
     * Shows given error message in primefaces message popup.
     * @param message Error message to show.
     */
    protected void showErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("dialogErrorTitle"), message));
    }
    

    
    /**
     * Checks if given categories contain duplicate names.
     * @param categories List of categories to check.
     * @return True if categories contain duplicates, otherwise false.
     */
    protected static boolean hasDuplicate(List<ObservationCategory> categories) {
        Set<String> set = new HashSet<>();
        for (ObservationCategory category : categories) {
            String name = category.getName();
            if (!name.isEmpty() && !set.add(category.getName())) {
                return true;
            }
        }
        return false;
    }

}
