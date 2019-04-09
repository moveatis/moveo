package com.moveatis.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

@Named(value = "analysisRecordTable")
@SessionScoped
public class FeebackAnalysisRecordTableManagedBean implements Serializable{
	
	private List<FeedbackAnalysisRecordEntity> selectedRecords;
	@Inject 
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean; 
	
	public String getSelectedCategorysName(List<FeedbackAnalysisCategoryEntity> selectedCategories, FeedbackAnalysisCategorySetEntity categorySet){
		for(FeedbackAnalysisCategoryEntity selectedCategory: selectedCategories){
			for(AbstractCategoryEntity cat : categorySet.getCategoryEntitys().values()){
				if(cat == selectedCategory){return selectedCategory.getLabel().getText();}
			}
		}
		return "empty";
	}
	
	public FeedbackAnalysisCategoryEntity getSelectedCategory(List<FeedbackAnalysisCategoryEntity> selectedCategories, FeedbackAnalysisCategorySetEntity categorySet){
		for(FeedbackAnalysisCategoryEntity selectedCategory: selectedCategories){
			for(AbstractCategoryEntity cat : categorySet.getCategoryEntitys().values()){
				if(cat == selectedCategory){return (FeedbackAnalysisCategoryEntity) selectedCategory;}
			}
		}
		return null;
	}

	public List<FeedbackAnalysisRecordEntity> getSelectedRecords() {
		return selectedRecords;
	}

	public void setSelectedRecords(List<FeedbackAnalysisRecordEntity> selectedRecords) {
		this.selectedRecords = selectedRecords;
	}
	
	
    public void deleteCurrentRecord(FeedbackAnalysisRecordEntity record){
    	List<FeedbackAnalysisRecordEntity> list = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity().getRecords();
    	if(list.contains(record)){
    		list.remove(record);
    		feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity().setRecords(list);
    	}
    }

		
}