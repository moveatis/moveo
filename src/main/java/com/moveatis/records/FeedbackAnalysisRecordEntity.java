package com.moveatis.records;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.moveatis.abstracts.AbstractRecordEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.observation.ObservationCategorySet;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name="FEEDBACKANALYSISRECORD")
public class FeedbackAnalysisRecordEntity extends AbstractRecordEntity {
	@ManyToOne
	private FeedbackAnalyzationEntity feedbackAnalyzation;

	@ManyToMany(cascade=MERGE)
	@JoinTable(name="FeedbackAnalysisRecordSelectedCategories")
	private List<FeedbackAnalysisCategoryEntity> selectedCategories;
	

	
	public void addSelectedCategory(FeedbackAnalysisCategoryEntity category) {
		selectedCategories.add(category);
		if(category.getRecordsContainingThisFeedbackAnalysisCategory()==null) 
			category.setRecordsContainingThisFeedbackAnalysisCategory(new ArrayList<FeedbackAnalysisRecordEntity>());
		category.getRecordsContainingThisFeedbackAnalysisCategory().add(this);
	}	
	public void removeSelectedCategory(FeedbackAnalysisCategoryEntity category) {
		selectedCategories.remove(category);
		category.getRecordsContainingThisFeedbackAnalysisCategory().remove(this);
	}	
    public List<FeedbackAnalysisCategoryEntity> getSelectedCategories() {
		return selectedCategories;
	}
	public void setSelectedCategories(List<FeedbackAnalysisCategoryEntity> selectedCategories) {
		this.selectedCategories=new ArrayList<FeedbackAnalysisCategoryEntity>();
		for(FeedbackAnalysisCategoryEntity selectedCategory:selectedCategories)
			addSelectedCategory(selectedCategory);
	}
	public FeedbackAnalyzationEntity getFeedbackAnalyzation() {
		return feedbackAnalyzation;
	}
	public void setFeedbackAnalyzation(FeedbackAnalyzationEntity feedbackAnalyzation) {
		this.feedbackAnalyzation = feedbackAnalyzation;
	}
	
}
