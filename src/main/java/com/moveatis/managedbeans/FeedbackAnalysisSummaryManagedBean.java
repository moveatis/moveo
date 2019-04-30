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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import static org.primefaces.model.chart.LegendPlacement.OUTSIDE;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

/**
 * The managed bean to control the summary page
 * 
 * @author Visa Nykänen
 */
@Named(value = "feedbackAnalysisSummaryManagedBean")
@ViewScoped
public class FeedbackAnalysisSummaryManagedBean implements Serializable {

	/**
	 * A helper class only useful in this view to hold the information used by the
	 * summary table
	 * 
	 * @author Visa Nykänen
	 */
	public class TableInformation {

		private String feedbackAnalysisCategorySet;

		private List<String> categories;

		private List<Integer> counts;

		public TableInformation(String feedbackAnalysisCategorySet) {
			this.categories = new ArrayList<String>();
			this.counts = new ArrayList<Integer>();
			this.setFeedbackAnalysisCategorySet(feedbackAnalysisCategorySet);
		}

		public void addCategoryWithCount(String category, Integer count) {
			this.categories.add(category);
			this.counts.add(count);
		}

		public String getFeedbackAnalysisCategorySet() {
			return feedbackAnalysisCategorySet;
		}

		public void setFeedbackAnalysisCategorySet(String feedbackAnalysisCategorySet) {
			this.feedbackAnalysisCategorySet = feedbackAnalysisCategorySet;
		}

		public List<String> getCategories() {
			return categories;
		}

		public void setCategories(List<String> categories) {
			this.categories = categories;
		}

		public List<Integer> getCounts() {
			return counts;
		}

		public void setCounts(List<Integer> counts) {
			this.counts = counts;
		}

	}

	private static final long serialVersionUID = 1L;

	private List<FeedbackAnalysisCategorySetEntity> categorySetsInUse;

	private FeedbackAnalyzationEntity feedbackAnalyzation;

	private List<BarChartModel> barModels;

	private List<PieChartModel> pieModels;

	private List<TableInformation> tableInformations;

	private boolean renderPieChart = false;

	private boolean renderBarChart = false;
	
	private final String SAVETODATABASE = "save";
	
	private final String SAVEASIMAGE = "image";
	
	private String emailAddress;
	
	private List<String> selectedSaveOperations;

	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<String> getSelectedSaveOperations() {
		return selectedSaveOperations;
	}

	public void setSelectedSaveOperations(List<String> selectedSaveOperations) {
		this.selectedSaveOperations = selectedSaveOperations;
	}
	
	public boolean isRenderPieChart() {
		return renderPieChart;
	}

	public void setRenderPieChart(boolean renderPieChart) {
		this.renderPieChart = renderPieChart;
	}

	public boolean isRenderBarChart() {
		return renderBarChart;
	}

	public void setRenderBarChart(boolean renderBarChart) {
		this.renderBarChart = renderBarChart;
	}

	public List<TableInformation> getTableInformations() {
		return tableInformations;
	}

	public void setTableInformations(List<TableInformation> tableInformations) {
		this.tableInformations = tableInformations;
	}

	public List<PieChartModel> getPieModels() {
		return pieModels;
	}

	public void setPieModels(List<PieChartModel> pieModels) {
		this.pieModels = pieModels;
	}

	public List<BarChartModel> getBarModels() {
		return barModels;
	}

	public void setBarModels(List<BarChartModel> barModels) {
		this.barModels = barModels;
	}

	public List<FeedbackAnalysisCategorySetEntity> getCategorySetsInUse() {
		return categorySetsInUse;
	}

	public void setCategorySetsInUse(List<FeedbackAnalysisCategorySetEntity> categorySetsInUse) {
		this.categorySetsInUse = categorySetsInUse;
	}

	public FeedbackAnalyzationEntity getFeedbackAnalyzation() {
		return feedbackAnalyzation;
	}

	public void setFeedbackAnalyzation(FeedbackAnalyzationEntity feedbackAnalyzation) {
		this.feedbackAnalyzation = feedbackAnalyzation;
	}

	public FeedbackAnalysisSummaryManagedBean() {

	}
	
	public boolean isSelected(String saveOperation) {
		for(String s : selectedSaveOperations)
			if(s.contentEquals(saveOperation))return true;
		return false;
	}
	
	public void save() {
		if(isSelected(SAVETODATABASE))
			feedbackAnalyzationManagedBean.saveFeedbackAnalyzation();
	}

	/**
	 * calls the initModels function to build the summary table and the charts
	 */
	@PostConstruct
	public void init() {
		selectedSaveOperations=new ArrayList<>();
		initSummary();
	}
	
	public String countPercentage(int count) {
		DecimalFormat df=new DecimalFormat("#.#");
		return df.format(100*(double)count/(double)feedbackAnalyzation.getRecords().size());
	}

	/**
	 * Gets the feedback analyzation from the feedbackanalyzatinomanagedbean and
	 * builds the summary table and the charts based on the information contained in
	 * it
	 */
	private void initSummary() {
		List<FeedbackAnalysisCategoryEntity> allSelectedCategories = new ArrayList<FeedbackAnalysisCategoryEntity>();
		feedbackAnalyzation = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity();
		categorySetsInUse = feedbackAnalyzationManagedBean.getFeedbackAnalysisCategorySetsInUse();
		int maxAxis = feedbackAnalyzation.getRecords().size();
		for (FeedbackAnalysisRecordEntity record : feedbackAnalyzation.getRecords()) {
			allSelectedCategories.addAll(record.getSelectedCategories());
		}

		List<BarChartModel> barModels = new ArrayList<BarChartModel>();
		List<PieChartModel> pieModels = new ArrayList<PieChartModel>();
		List<TableInformation> tableInformations = new ArrayList<TableInformation>();

		for (FeedbackAnalysisCategorySetEntity catSet : categorySetsInUse) {
			BarChartModel barModel = new BarChartModel();
			PieChartModel pieModel = new PieChartModel();
			TableInformation tableInformation = new TableInformation(catSet.getLabel());
			int fullcount = 0;
			for (AbstractCategoryEntity cat : catSet.getCategoryEntitys().values()) {
				ChartSeries categorySetChartSeries = new ChartSeries();
				categorySetChartSeries.setLabel(cat.getLabel().getText());
				int count = 0;
				// Comparison by category name and categoryset-name, because if the analyzation
				// hasn't yet been saved to the database the ID is null
				// categoryset-category pairs have to be unique
				for (FeedbackAnalysisCategoryEntity cat_comp : allSelectedCategories)
					if (cat.getLabel().getText().contentEquals(cat_comp.getLabel().getText())
							&& catSet.getLabel().contentEquals(cat_comp.getCategorySet().getLabel()))
						count++;
				fullcount += count;

				pieModel.set(cat.getLabel().getText(), count);
				categorySetChartSeries.set("", count);

				barModel.addSeries(categorySetChartSeries);

				tableInformation.addCategoryWithCount(cat.getLabel().getText(), count);
			}
			if (maxAxis > fullcount) {
				ChartSeries empty = new ChartSeries();
				empty.setLabel("empty");
				empty.set(catSet.getLabel(), maxAxis - fullcount);
				barModel.addSeries(empty);
				pieModel.set("empty", maxAxis - fullcount);
				tableInformation.addCategoryWithCount("empty", maxAxis - fullcount);
			}
			pieModel.setTitle(catSet.getLabel());
			pieModel.setLegendPlacement(OUTSIDE);
			pieModel.setLegendPosition("s");
			
			barModel.setBarWidth(50);
			barModel.setTitle(catSet.getLabel());
			barModel.setStacked(true);
			barModel.setLegendPlacement(OUTSIDE);
			barModel.setLegendPosition("s");
			Axis yAxis = barModel.getAxis(AxisType.Y);
			yAxis.setMin(0);
			yAxis.setTickFormat("%3d");
			yAxis.setTickInterval("1");
			yAxis.setMax(maxAxis);
			if(catSet!=categorySetsInUse.get(0))barModel.setExtender("chartExtenderHideTicks");
			else barModel.setExtender("chartExtender");

			barModels.add(barModel);
			pieModels.add(pieModel);
			tableInformations.add(tableInformation);
		}

		this.barModels = barModels;
		this.pieModels = pieModels;
		this.tableInformations = tableInformations;
	}
	
	public int countMaxCategories() {
		int max=0;
		for (FeedbackAnalysisCategorySetEntity catSet:categorySetsInUse)
			if(catSet.getCategoryEntitys().size()>max)
				max=catSet.getCategoryEntitys().size();
		return max+1;
	}
}