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
	private FeedbackAnalyzationEntity FeedbackAnalyzation;
    public FeedbackAnalyzationEntity getFeedbackAnalyzation() {
		return FeedbackAnalyzation;
	}
	public void setFeedbackAnalyzation(FeedbackAnalyzationEntity feedbackAnalyzation) {
		FeedbackAnalyzation = feedbackAnalyzation;
	}
	public Set<FeedbackAnalysisRecordSelectedCategory> getSelectedCategories() {
		return SelectedCategories;
	}
	public void setSelectedCategories(Set<FeedbackAnalysisRecordSelectedCategory> selectedCategories) {
		SelectedCategories = selectedCategories;
	}
	@ElementCollection(fetch = FetchType.LAZY)
    private Set<FeedbackAnalysisRecordSelectedCategory> SelectedCategories;
}
