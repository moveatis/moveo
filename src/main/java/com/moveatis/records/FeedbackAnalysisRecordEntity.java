package com.moveatis.records;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.moveatis.abstracts.AbstractRecordEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.observation.ObservationCategorySet;


@Entity
@Table(name="FEEDBACKANALYSISRECORD")
public class FeedbackAnalysisRecordEntity extends AbstractRecordEntity {
	@ManyToOne
	private FeedbackAnalyzationEntity feedbackAnalyzation;
    private Set<FeedbackAnalysisRecordSelectedCategory> SelectedCategories;
    public FeedbackAnalyzationEntity getFeedbackAnalyzation() {
		return feedbackAnalyzation;
	}
	public void setFeedbackAnalyzation(FeedbackAnalyzationEntity feedbackAnalyzation) {
		this.feedbackAnalyzation = feedbackAnalyzation;
	}
	public Set<FeedbackAnalysisRecordSelectedCategory> getSelectedCategories() {
		return SelectedCategories;
	}
	public void setSelectedCategories(Set<FeedbackAnalysisRecordSelectedCategory> selectedCategories) {
		SelectedCategories = selectedCategories;
	}
}
