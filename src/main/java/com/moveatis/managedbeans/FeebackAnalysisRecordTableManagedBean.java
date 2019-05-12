/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * Copyright (c) 2019, Visa Nykänen, Tuomas Moisio, Petra Puumala, Karoliina Lappalainen
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.managedbeans;


import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.helpers.DownloadTools;
import com.moveatis.interfaces.FeedbackAnalyzation;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

/**
 * @author tusamois
 *
 */

@Named(value = "analysisRecordTable")
@ViewScoped
public class FeebackAnalysisRecordTableManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;
	private FeedbackAnalysisRecordEntity selectedRow;
	private List<String> selectedSaveOptions;
	private FeedbackAnalyzationEntity feedbackAnalyzation;

	private String fileName;

	@Inject
	private FeedbackAnalyzation feedbackAnalyzationEJB;

	private static final Logger LOGGER = LoggerFactory.getLogger(SummaryManagedBean.class);

	/**
	 * The post constructor creates the feedback analyzation
	 */
	@PostConstruct
	protected void initialize() {
		feedbackAnalyzation = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity();
	}

	/**
	 * Gets the name of selected category from current category set
	 * 
	 * @param selectedCategories
	 *            Users selected categories
	 * @param categorySet
	 *            Category set in use
	 * @return name of the selected category, empty if no category is selected
	 */
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

		return "------";
	}

	/**
	 * Gets the selected category from current category set
	 * 
	 * @param selectedCategories
	 *            Users selected categories
	 * @param categorySet
	 *            Category set in use
	 * @return the selected category, new category if the category is not selected
	 */
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
		FeedbackAnalysisCategoryEntity value = new FeedbackAnalysisCategoryEntity();
		return value;
	}

	/**
	 * Gets the selected row from datatable
	 * 
	 * @return seleceted row
	 */
	public FeedbackAnalysisRecordEntity getSelectedRow() {
		return selectedRow;
	}

	/**
	 * Sets the selected row to datatable
	 */
	public void setSelectedRow(FeedbackAnalysisRecordEntity selectedRow) {
		this.selectedRow = selectedRow;
	}

	/**
	 * Delete's the selected row from the datatable
	 * 
	 * @param record
	 *            selected row
	 */
	public void delete(int orderNumber) {
		if (feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity().getRecords().size() == 1) {
			RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Deletion failed", "There needs to be at least one record in an analyzation."));
			return;
		}

		List<FeedbackAnalysisRecordEntity> list = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity()
				.getRecords();
		FeedbackAnalysisRecordEntity record=null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getOrderNumber() != null && list.get(i).getOrderNumber().intValue() == orderNumber) {
				record=list.get(i);
				list.remove(i);
				break;
			}
		}
		setOrderNumbers(list);
		feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity().setRecords(list);				
		if (record!=null&&record.getId() != null)
					feedbackAnalyzationEJB.removeRecordFromAnalyzation(
							feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity(),record);
		feedbackAnalyzationManagedBean.setCurrentRecord(list.size());
	}

	/**
	 * Sends the user to the analyzer page with the selected record as main record.
	 * 
	 * @param orderNumber
	 *            order number of the selected record
	 * @return String that faces-config uses to control navigation
	 */
	public String edit(Integer orderNumber) {
		List<FeedbackAnalysisRecordEntity> list = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity()
				.getRecords();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getOrderNumber().intValue() == orderNumber.intValue()) {
				feedbackAnalyzationManagedBean.setCurrentRecord(i + 1);
			}
		}
		return "editrow";
	}

	/**
	 * Comparator for feedbackanalysisrecords, compares the ordernumber
	 * 
	 * @author Visa Nykänen
	 *
	 */
	private class compareRecords implements Comparator<FeedbackAnalysisRecordEntity> {
		@Override
		public int compare(FeedbackAnalysisRecordEntity a, FeedbackAnalysisRecordEntity b) {
			return a.getOrderNumber().compareTo(b.getOrderNumber());
		}
	}

	/**
	 * Updates order numbers to records list
	 * 
	 * @param list
	 *            users records
	 */
	private void setOrderNumbers(List<FeedbackAnalysisRecordEntity> list) {
		list.sort(new compareRecords());
		Integer newOrderNumber = 1;
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setOrderNumber(i + 1);
			newOrderNumber++;
		}
	}

	/**
	 * Getter
	 * 
	 * @return
	 */
	public List<String> getSelectedSaveOptions() {
		return selectedSaveOptions;
	}

	/**
	 * Setter
	 * 
	 * @param selectedSaveOptions
	 */
	public void setSelectedSaveOptions(List<String> selectedSaveOptions) {
		this.selectedSaveOptions = selectedSaveOptions;
	}

	public void downloadImage() {
		File img = DownloadTools.getImageFromByteArr(
				feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity().getAnalyzationName() + "_report_",
				feedbackAnalyzationManagedBean.getReportImage());
		DownloadTools.downloadFile(img, "image/png",
				img.getName().substring(0, img.getName().lastIndexOf("_")) + ".png");
		img.delete();
	}

	/**
	 * Converts feedback analyzation's name to filename
	 * 
	 * @param s
	 * @return
	 */
	private static String convertToFilename(String s) {
		if (s == null || s.isEmpty()) {
			return "unnamed";
		}
		return s.replaceAll("[^a-zA-Z0-9_]", "_");
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
