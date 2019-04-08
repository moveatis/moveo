package com.moveatis.managedbeans;

import java.io.Serializable;
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

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

@Named(value = "feedbackAnalysisSummaryManagedBean")
@ViewScoped
public class FeedbackAnalysisSummaryManagedBean implements Serializable {
	/**
	 * 
	 */
	
	public class TableInformation{
		private String feedbackAnalysisCategorySet;
		private List<String> categories;
		private List<Integer> counts;
		
		public TableInformation(String feedbackAnalysisCategorySet) {
			this.categories=new ArrayList<String>();
			this.counts=new ArrayList<Integer>();
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
	private boolean renderPieChart=false;
	private boolean renderBarChart=false;
	
	
	
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

	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;

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
	



	@PostConstruct
	public void init() {
		initBarModels();
	}

	private void initBarModels() {

		List<FeedbackAnalysisCategoryEntity> allSelectedCategories = new ArrayList<FeedbackAnalysisCategoryEntity>();
		feedbackAnalyzation = feedbackAnalyzationManagedBean.getFeedbackAnalyzationEntity();
		categorySetsInUse = feedbackAnalyzationManagedBean.getFeedbackAnalysisCategorySetsInUse();
		int maxAxis = feedbackAnalyzation.getRecords().size();
		for (FeedbackAnalysisRecordEntity record : feedbackAnalyzation.getRecords()) {
			allSelectedCategories.addAll(record.getSelectedCategories());
		}

		List<BarChartModel> barModels = new ArrayList<BarChartModel>();
		List<PieChartModel> pieModels = new ArrayList<PieChartModel>();
		List<TableInformation> tableInformations=new ArrayList<TableInformation>();
		
		for (FeedbackAnalysisCategorySetEntity catSet : categorySetsInUse) {
			BarChartModel barModel = new BarChartModel();
			PieChartModel pieModel = new PieChartModel();
			TableInformation tableInformation=new TableInformation(catSet.getLabel());
			int fullcount = 0;
			for (AbstractCategoryEntity cat : catSet.getCategoryEntitys().values()) {
				ChartSeries categorySetChartSeries = new ChartSeries();
				categorySetChartSeries.setLabel(cat.getLabel().getText());
				int count = 0;
				for (FeedbackAnalysisCategoryEntity cat_comp : allSelectedCategories)
					if (cat.getId() == cat_comp.getId())
						count++;
				fullcount += count;

				pieModel.set(cat.getLabel().getText(), count);
				categorySetChartSeries.set(catSet.getLabel(), count);

				barModel.addSeries(categorySetChartSeries);
				
				tableInformation.addCategoryWithCount(cat.getLabel().getText(),count);
			}
			if (maxAxis > fullcount) {
				ChartSeries empty = new ChartSeries();
				empty.setLabel("empty");
				empty.set(catSet.getLabel(), maxAxis - fullcount);
				barModel.addSeries(empty);
				pieModel.set("empty", maxAxis - fullcount);
				tableInformation.addCategoryWithCount("empty",maxAxis-fullcount);
			}
			pieModel.setTitle(catSet.getLabel());
			pieModel.setLegendPosition("ne");
			
			barModel.setStacked(true);
			barModel.setLegendPosition("m");
			Axis yAxis = barModel.getAxis(AxisType.Y);
			yAxis.setMin(0);
			yAxis.setTickFormat("%3d");
			yAxis.setTickInterval("1");
			yAxis.setMax(maxAxis);
			
			barModels.add(barModel);
			pieModels.add(pieModel);
			tableInformations.add(tableInformation);
		}

		this.barModels= barModels;
		this.pieModels=pieModels;
		this.tableInformations=tableInformations;
	}


}
