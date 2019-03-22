package com.moveatis.records;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.moveatis.abstracts.BaseEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationCategorySet;
public class FeedbackAnalysisRecordSelectedCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FeedbackAnalysisCategoryEntity selectedCategory;
	private FeedbackAnalysisCategorySetEntity categoryset;
	
	public FeedbackAnalysisCategoryEntity getSelectedCategory() {
		return selectedCategory;
	}
	public void setSelectedCategory(FeedbackAnalysisCategoryEntity selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
	public FeedbackAnalysisCategorySetEntity getCategoryset() {
		return categoryset;
	}
	public void setCategoryset(FeedbackAnalysisCategorySetEntity categoryset) {
		this.categoryset = categoryset;
	}

}
