package com.moveatis.records;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.moveatis.abstracts.BaseEntity;
import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationCategorySet;
public class FeedbackAnalysisRecordSelectedCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObservationCategory selectedCategory;
	private ObservationCategorySet categoryset;
	
	public ObservationCategory getSelectedCategory() {
		return selectedCategory;
	}
	public void setSelectedCategory(ObservationCategory selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
	public ObservationCategorySet getCategoryset() {
		return categoryset;
	}
	public void setCategoryset(ObservationCategorySet categoryset) {
		this.categoryset = categoryset;
	}

}
