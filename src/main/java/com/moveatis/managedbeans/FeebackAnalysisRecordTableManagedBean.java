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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.export.CSVFileBuilder;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

/**
 * @author tusamois
 *
 */

@Named(value = "analysisRecordTable")
@SessionScoped
public class FeebackAnalysisRecordTableManagedBean implements Serializable {

	private List<FeedbackAnalysisRecordEntity> selectedRecords;
	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;
	private FeedbackAnalysisRecordEntity selectedRow;
	private List<String> selectedSaveOptions;
	private static final String DOWNLOAD_OPTION = "download";
	private static final String IMAGE_OPTION = "image";
	private FeedbackAnalyzationEntity feedbackAnalyzation;

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

		return "empty";
	}

	/**
	 * Gets the selected category from current category set
	 * 
	 * @param selectedCategories
	 *            Users selected categories
	 * @param categorySet
	 *            Category set in use
	 * @return the selected category, new category if the category is not
	 *         selected
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
	 * Delete's the selected row from the datatable
	 * 
	 * @param record
	 *            selected row
	 */
	public void deleteCurrentRecord(FeedbackAnalysisRecordEntity record) {
		List<FeedbackAnalysisRecordEntity> list = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity()
				.getRecords();
		list.remove(record);
		FeedbackAnalyzationEntity feedbackAnalyzationEntity = feedbackAnalyzationManagedBean
				.getFeedbackAnalyzationEntity();
		feedbackAnalyzationEntity.setRecords(list);
		feedbackAnalyzationManagedBean.setFeedbackAnalyzationEntity(feedbackAnalyzationEntity);
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
	public void delete() {
		List<FeedbackAnalysisRecordEntity> list = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity()
				.getRecords();
		list.remove(selectedRow);
		selectedRow.setSelectedCategories(null);
		feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity().setRecords(list);
		selectedRow = null;
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

	/**
	 * Do all the save operations selected by the user.
	 */
	public void doSelectedSaveOperation() {
		if (selectedSaveOptions.contains(DOWNLOAD_OPTION)) {
			try {
				downloadCurrentObservation();
			} catch (IOException e) {
				LOGGER.error("Failed to download the observation.", e);
			}
		}
		if (selectedSaveOptions.contains(IMAGE_OPTION)) {
			// TODO: saveCurrentObservation();
		}
	}

	/**
	 * Downloads the feedback analyzation as csv
	 * 
	 * @throws IOException
	 */
	private void downloadCurrentObservation() throws IOException {
		String fileName = convertToFilename(feedbackAnalyzation.getName()) + ".csv";

		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext externalCtx = facesCtx.getExternalContext();

		externalCtx.responseReset();
		externalCtx.setResponseContentType("text/plain");
		externalCtx.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		OutputStream outputStream = externalCtx.getResponseOutputStream();

		CSVFileBuilder csv = new CSVFileBuilder();
		csv.buildCSV(outputStream, feedbackAnalyzation, ",");
		outputStream.flush();

		facesCtx.responseComplete();
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

}