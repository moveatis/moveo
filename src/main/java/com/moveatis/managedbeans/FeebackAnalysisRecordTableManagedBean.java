package com.moveatis.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

@Named(value = "analysisRecordTable")
@SessionScoped
public class FeebackAnalysisRecordTableManagedBean implements Serializable {

	private List<FeedbackAnalysisRecordEntity> selectedRecords;
	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;
	private FeedbackAnalysisRecordEntity selectedRow;

	public String getSelectedCategorysName(List<FeedbackAnalysisCategoryEntity> selectedCategories,
			FeedbackAnalysisCategorySetEntity categorySet) {
		for (FeedbackAnalysisCategoryEntity cat_comp : selectedCategories) {
			for (AbstractCategoryEntity cat : categorySet.getCategoryEntitys().values()) {
				if (cat.getLabel().getText().contentEquals(cat_comp.getLabel().getText())
						&& cat.getCategorySet().getLabel().contentEquals(cat_comp.getCategorySet().getLabel())) {
					return cat.getLabel().getText();
				}
			}
		}

		return "empty";
	}

	public FeedbackAnalysisCategoryEntity getSelectedCategory(List<FeedbackAnalysisCategoryEntity> selectedCategories,
			FeedbackAnalysisCategorySetEntity categorySet) {
		for (FeedbackAnalysisCategoryEntity cat_comp : selectedCategories) {
			for (AbstractCategoryEntity cat : categorySet.getCategoryEntitys().values()) {
				if (cat.getLabel().getText().contentEquals(cat_comp.getLabel().getText())
						&& cat.getCategorySet().getLabel().contentEquals(cat_comp.getCategorySet().getLabel())) {
					return (FeedbackAnalysisCategoryEntity) cat_comp;
				}
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

	public void deleteCurrentRecord(FeedbackAnalysisRecordEntity record) {
		List<FeedbackAnalysisRecordEntity> list = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity()
				.getRecords();
		list.remove(record);
		FeedbackAnalyzationEntity feedbackAnalyzationEntity = feedbackAnalyzationManagedBean
				.getFeedbackAnalyzationEntity();
		feedbackAnalyzationEntity.setRecords(list);
		feedbackAnalyzationManagedBean.setFeedbackAnalyzationEntity(feedbackAnalyzationEntity);
	}

	public FeedbackAnalysisRecordEntity getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(FeedbackAnalysisRecordEntity selectedRow) {
		this.selectedRow = selectedRow;
	}

	public void delete() {
		List<FeedbackAnalysisRecordEntity> list = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity()
				.getRecords();
		list.remove(selectedRow);
		selectedRow.setSelectedCategories(null);
		feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity().setRecords(list);
		selectedRow = null;
	}

}