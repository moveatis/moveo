package com.moveatis.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.moveatis.interfaces.CategorySet;
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
    private String newFeedbackAnalysisCategorySetName;
    
    @Inject @MessageBundle //created MessageBundle to allow resourcebundle injection to CDI beans
    private transient ResourceBundle messages;  //RequestBundle is not serializable 
    
    private long selectedDefaultFeedbackAnalysisCategorySet;
    private long selectedPrivateFeedbackAnalysisCategorySet;
    
    
    
    private List<FeedbackAnalysisCategorySetEntity> defaultFeedbackAnalysisCategorySets; // From group key or event that was selected in control page.
    private List<FeedbackAnalysisCategorySetEntity>  privateFeedbackAnalysisCategorySets;
    private List<FeedbackAnalysisCategorySetEntity>  feedbackAnalysisCategorySetsInUse;
    
    private EventGroupEntity eventGroup;
    
    @Inject
    private Session sessionEJB;
    @Inject
    private EventGroup eventGroupEJB;
	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;

	private CategorySet categorySetEJB;
	
	/**
	 * Gets the categorysets from the given groupkey, 
	 * and if the user is logged in their own categorysets
	 * */
	@PostConstruct
	public void init() {
		eventGroup = null;
		defaultFeedbackAnalysisCategorySets=new ArrayList<FeedbackAnalysisCategorySetEntity>(); 
        privateFeedbackAnalysisCategorySets=new ArrayList<FeedbackAnalysisCategorySetEntity>();
        feedbackAnalysisCategorySetsInUse=new ArrayList<FeedbackAnalysisCategorySetEntity>();  
        if (feedbackAnalyzationManagedBean.getEventEntity() != null) {
            EventEntity event = feedbackAnalyzationManagedBean.getEventEntity();
            eventGroup = event.getEventGroup();
            defaultFeedbackAnalysisCategorySets=eventGroup.getFeedbackAnalysisCategorySets();
        }

        if (sessionEJB.isIdentifiedUser()) {
            IdentifiedUserEntity user = sessionEJB.getLoggedIdentifiedUser();
            for (EventGroupEntity eg: eventGroupEJB.findAllForOwner(user))
            	for (FeedbackAnalysisCategorySetEntity fba:eg.getFeedbackAnalysisCategorySets())
            		privateFeedbackAnalysisCategorySets.add(fba);
        }
        
        List<FeedbackAnalysisCategorySetEntity> categorySets = sessionEJB.getFeedbackAnalysisCategorySetsInUse();
        if (categorySets != null) {
            feedbackAnalysisCategorySetsInUse=categorySets;
        } else {
            for(FeedbackAnalysisCategorySetEntity categorySet : defaultFeedbackAnalysisCategorySets) {
                feedbackAnalysisCategorySetsInUse.add(categorySet);
            }
        }
    }
	




	/**
     * Adds a new category set for the analyzation if newFeedbackAnalysisCategorySetName isn't empty.
     */
    public void addNewCategorySet() {
        String name = Validation.validateForJsAndHtml(newFeedbackAnalysisCategorySetName);
        
        if (!name.isEmpty()) {
            for (FeedbackAnalysisCategorySetEntity set : feedbackAnalysisCategorySetsInUse) {
                if (name.equals(set.getLabel())) {
                    showErrorMessage(messages.getString("cs_errorNotUniqueCategorySet"));
                    return;
                }
            }
            
            FeedbackAnalysisCategorySetEntity categorySet = new FeedbackAnalysisCategorySetEntity();
            categorySet.setLabel(name);
            Map<Integer, AbstractCategoryEntity> newCategoryEntities=new TreeMap<Integer,AbstractCategoryEntity>();
            categorySet.setCategoryEntitys(newCategoryEntities);
            feedbackAnalysisCategorySetsInUse.add(categorySet);
            newFeedbackAnalysisCategorySetName = "";
        }
    }
    
    /**
     * creates a copy of the category set when the user edits it
     * prevents the user from making changes to category sets gotten with a group key
     * */
    public FeedbackAnalysisCategorySetEntity getCopyForEditing(FeedbackAnalysisCategorySetEntity categorySet) {
    		if(categorySet.getId()==null)return categorySet;
    		feedbackAnalysisCategorySetsInUse.remove(categorySet);
    		FeedbackAnalysisCategorySetEntity tmp_categorySet = new FeedbackAnalysisCategorySetEntity();
    		Map<Integer,AbstractCategoryEntity> categoryEntities=categorySet.getCategoryEntitys();
    		Map<Integer,AbstractCategoryEntity> newCategoryEntities=new TreeMap<Integer,AbstractCategoryEntity>();
    		for( int key: categoryEntities.keySet()) {
    			FeedbackAnalysisCategoryEntity cur_cat=(FeedbackAnalysisCategoryEntity) categoryEntities.get(key);
    			FeedbackAnalysisCategoryEntity tmp_cat=new FeedbackAnalysisCategoryEntity();
    			tmp_cat.setCategorySet(tmp_categorySet);
    			LabelEntity label =new LabelEntity();
    			label.setText(cur_cat.getLabel().getText());
    			tmp_cat.setLabel(label);
    			tmp_cat.setDescription(cur_cat.getDescription());
    			tmp_cat.setOrderNumber(key);
    			newCategoryEntities.put(key, tmp_cat);
    		}
    		tmp_categorySet.setLabel(categorySet.getLabel());
    		tmp_categorySet.setDescription(categorySet.getDescription());
    		tmp_categorySet.setCategoryEntitys(newCategoryEntities);
    		
    		feedbackAnalysisCategorySetsInUse.add(tmp_categorySet);
    		return tmp_categorySet;
    }
    /**
     * Adds a new category to the given categoryset
     * */
    public void addNewCategoryToCategorySet(FeedbackAnalysisCategorySetEntity categorySet) {
    	FeedbackAnalysisCategoryEntity fac=new FeedbackAnalysisCategoryEntity();
    	if(categorySet.getId()!=null) categorySet=getCopyForEditing(categorySet);
    	fac.setLabel(new LabelEntity());
    	
    	Map<Integer, AbstractCategoryEntity> categories=categorySet.getCategoryEntitys();
    	fac.setOrderNumber(categories.keySet().size());
    	fac.setCategorySet(categorySet);
    	categories.put(categories.keySet().size(),fac);
    }

    /**
     * removes the given category from the given categoryset
     * */
    public void removeCategoryFromCategorySet(FeedbackAnalysisCategorySetEntity categorySet, FeedbackAnalysisCategoryEntity category) {
    	if(categorySet.getId()!=null) categorySet=getCopyForEditing(categorySet);
    	Map<Integer, AbstractCategoryEntity> categories=categorySet.getCategoryEntitys();
    	Map<Integer, AbstractCategoryEntity> tmp_categories=new TreeMap<Integer,AbstractCategoryEntity>();

    	categories.remove(category.getOrderNumber());    	
    	int i=0;    	
    	for(int key : categories.keySet()) {
    		categories.get(key).setOrderNumber(i);
    		tmp_categories.put(i,categories.get(key));
    		i++;
    	}
    	categorySet.setCategoryEntitys(tmp_categories);
    }
    
    /**
     * gets 
     * */
    private FeedbackAnalysisCategorySetEntity findById(List<FeedbackAnalysisCategorySetEntity> categorySets,long id) {
    	for (FeedbackAnalysisCategorySetEntity facs : categorySets){
    		if(facs.getId()==selectedPrivateFeedbackAnalysisCategorySet) {
    			return(facs);
    		}
    	}
    	return null;    	
    }
       /**
     * Adds the selected default category set for the analyzation.
     */
    public void addDefaultCategorySet() {
    	FeedbackAnalysisCategorySetEntity sdc=findById(defaultFeedbackAnalysisCategorySets,selectedDefaultFeedbackAnalysisCategorySet);
        if (!feedbackAnalysisCategorySetsInUse.contains(sdc)) {
            feedbackAnalysisCategorySetsInUse.add(sdc);
        }
    }
     
    /**
     * Adds the selected private category set for the observation.
     */
    public void addPrivateCategorySet() {
    	FeedbackAnalysisCategorySetEntity spc=findById(privateFeedbackAnalysisCategorySets,selectedPrivateFeedbackAnalysisCategorySet);
        if (!feedbackAnalysisCategorySetsInUse.contains(spc)) {
            feedbackAnalysisCategorySetsInUse.add(spc);
        }
    }
    
    /**
     * Removes the given category set from the observation.
     */
    public void removeCategorySet(FeedbackAnalysisCategorySetEntity categorySet) {
        feedbackAnalysisCategorySetsInUse.remove(categorySet);
    }
    
    /**
     * Checks if the continue button should be disabled.
     * The button is disabled if no category sets have been added for the observation
     * or if some of the added category sets are empty.
     * @return True if the continue button should be disabled.
     */
    public boolean isContinueDisabled() {
        for (FeedbackAnalysisCategorySetEntity categorySet : feedbackAnalysisCategorySetsInUse) {
            if (categorySet.getCategoryEntitys().isEmpty()) return true;
        }
        return feedbackAnalysisCategorySetsInUse.isEmpty();
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
     * @return "analysiscategoriesok" if the categories were ok, otherwise an empty string.
     */
    public String checkCategories() {
        boolean atLeastOneCategorySelected = false;
        
        for (FeedbackAnalysisCategorySetEntity categorySet : feedbackAnalysisCategorySetsInUse) {
            
            Map<Integer, AbstractCategoryEntity> categories = categorySet.getCategoryEntitys();
            
            if(hasDuplicate(categories)) {
                showErrorMessage(messages.getString("cs_errorNotUniqueCategories"));
                return "";
            }
            
            if (!categories.isEmpty()) {
                atLeastOneCategorySelected = true;
            } else {
                showErrorMessage(messages.getString("cs_warningEmptyCategorySets"));
                return ""; 
            }
            
            for (AbstractCategoryEntity category : categories.values()) {
                
                if (category.getLabel().getText().isEmpty()) {
                    showErrorMessage(messages.getString("cs_warningEmptyCategories"));
                    return ""; 
                }
            }
        }
        
        if (!atLeastOneCategorySelected) {
            showErrorMessage(messages.getString("cs_errorNoneSelected"));
            return "";
        }

        feedbackAnalyzationManagedBean.setFeedbackAnalysisCategorySetsInUse(feedbackAnalysisCategorySetsInUse);
        return "analysiscategoriesok";
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
    
    
    public String getNewFeedbackAnalysisCategorySetName() {
		return newFeedbackAnalysisCategorySetName;
	}



	public void setNewFeedbackAnalysisCategorySetName(String newFeedbackAnalysisCategorySetName) {
		this.newFeedbackAnalysisCategorySetName = newFeedbackAnalysisCategorySetName;
	}



	public long getSelectedDefaultFeedbackAnalysisCategorySet() {
		return selectedDefaultFeedbackAnalysisCategorySet;
	}



	public void setSelectedDefaultFeedbackAnalysisCategorySet(long selectedDefaultFeedbackAnalysisCategorySet) {
		this.selectedDefaultFeedbackAnalysisCategorySet = selectedDefaultFeedbackAnalysisCategorySet;
	}



	public long getSelectedPrivateFeedbackAnalysisCategorySet() {
		return selectedPrivateFeedbackAnalysisCategorySet;
	}



	public void setSelectedPrivateFeedbackAnalysisCategorySet(long selectedPrivateFeedbackAnalysisCategorySet) {
		this.selectedPrivateFeedbackAnalysisCategorySet = selectedPrivateFeedbackAnalysisCategorySet;
	}



	public List<FeedbackAnalysisCategorySetEntity> getDefaultFeedbackAnalysisCategorySets() {
		return defaultFeedbackAnalysisCategorySets;
	}



	public void setDefaultFeedbackAnalysisCategorySets(
			List<FeedbackAnalysisCategorySetEntity> defaultFeedbackAnalysisCategorySets) {
		this.defaultFeedbackAnalysisCategorySets = defaultFeedbackAnalysisCategorySets;
	}



	public List<FeedbackAnalysisCategorySetEntity> getPrivateFeedbackAnalysisCategorySets() {
		return privateFeedbackAnalysisCategorySets;
	}



	public void setPrivateFeedbackAnalysisCategorySets(
			List<FeedbackAnalysisCategorySetEntity> privateFeedbackAnalysisCategorySets) {
		this.privateFeedbackAnalysisCategorySets = privateFeedbackAnalysisCategorySets;
	}



	public List<FeedbackAnalysisCategorySetEntity> getFeedbackAnalysisCategorySetsInUse() {
		return feedbackAnalysisCategorySetsInUse;
	}



	public void setFeedbackAnalysisCategorySetsInUse(
			List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse) {
		this.feedbackAnalysisCategorySetsInUse = feedbackAnalysisCategorySetsInUse;
	}



	public EventGroupEntity getEventGroup() {
		return eventGroup;
	}



	public void setEventGroup(EventGroupEntity eventGroup) {
		this.eventGroup = eventGroup;
	}



	public FeedbackAnalyzationManagedBean getFeedbackAnalyzationManagedBean() {
		return feedbackAnalyzationManagedBean;
	}



	public void setFeedbackAnalyzationManagedBean(FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean) {
		this.feedbackAnalyzationManagedBean = feedbackAnalyzationManagedBean;
	}

    
	}

