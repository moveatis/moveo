package com.moveatis.managedbeans;

import com.moveatis.interfaces.Label;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.interfaces.CategorySet;
import com.moveatis.interfaces.FeedbackAnalysisRecord;
import com.moveatis.interfaces.FeedbackAnalyzation;
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Session;
import com.moveatis.label.LabelEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

@Named(value = "feedbackAnalyzationManagedBean")
@SessionScoped
public class FeedbackAnalyzationManagedBean implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObservationManagedBean.class);

	private static final long serialVersionUID = 1L;

	@Inject
	private Label labelEJB;

	/*
	 * The feedbackanalyzationentity being edited
	 */
	private FeedbackAnalyzationEntity feedbackAnalyzationEntity;
	/*
	 * The categorysets being used in the analyzation event
	 */
	private List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse;

	/*
	 * The number of records currently added to the analyzation TODO get this based
	 * on the length of the list of records
	 */
	private int numberOfRecords;
	/*
	 * The index(+1) of the record currently in view
	 */
	private int currentRecordNumber;
	/*
	 * If the analyzation has some new categorysets they need to be saved, so
	 * CategorySetBean is needed
	 */
	@Inject
	private CategorySet categorySetEJB;
	/*
	 * The record currently in view
	 */
	private FeedbackAnalysisRecordEntity currentRecord;

	/*
	 * The event the analyzation is performed for
	 */
	private EventEntity eventEntity;
	/*
	 * used to save the analyzation to the database
	 */
	@EJB
	private FeedbackAnalyzation feedbackAnalyzationEJB;
	/*
	 * The categorysets are gotten from the session
	 */
	@Inject
	private Session sessionBean;
	/*
	 * The comment for the record currently in view
	 */
	private String comment;

	private FeedbackAnalysisCategoryEntity selectedCategory;

	@Inject
	private FeedbackAnalysisRecord feedbackAnalysisRecordEJB;

	public void setSelectedCategory(FeedbackAnalysisCategoryEntity selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public FeedbackAnalysisCategoryEntity getSelectedCategory() {
		return selectedCategory;
	}

	public void addCategoryToCurrentRecord(FeedbackAnalysisCategoryEntity category) {
		currentRecord.addSelectedCategory(category);
	}

	public void setCurrentRecord(FeedbackAnalysisRecordEntity currentRecord) {
		for (FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
			for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
				((FeedbackAnalysisCategoryEntity) fac).setInRecord(false);
		comment = currentRecord.getComment();

		List<FeedbackAnalysisCategoryEntity> selectedCategories = currentRecord.getSelectedCategories();
		for (FeedbackAnalysisCategoryEntity category : selectedCategories)
			category.setInRecord(true);
		this.currentRecord = currentRecord;
	}

	public FeedbackAnalysisRecordEntity getCurrentRecord() {
		return currentRecord;
	}

	/**
	 * Sets the record to be shown in the view based on the given parameter TODO:
	 * Make the list of records be ordered either by an order number or starttime
	 * (if the timer is implemented) as the order might change
	 * 
	 * @param recordNumber The index(+1) of the record to be accessed
	 */
	public void setCurrentRecord(int recordNumber) {
		if (recordNumber > numberOfRecords || recordNumber < 1 || recordNumber == currentRecordNumber)
			return;
		editRecord();
		currentRecordNumber = recordNumber;
		currentRecord = feedbackAnalyzationEntity.getRecords().get(recordNumber - 1);
		for (FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
			for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
				((FeedbackAnalysisCategoryEntity) fac).setInRecord(false);
		comment = currentRecord.getComment();

		List<FeedbackAnalysisCategoryEntity> selectedCategories = currentRecord.getSelectedCategories();
		for (FeedbackAnalysisCategoryEntity category : selectedCategories)
			category.setInRecord(true);
	}

	public List<FeedbackAnalysisCategorySetEntity> getFeedbackAnalysisCategorySetsInUse() {
		return feedbackAnalysisCategorySetsInUse;
	}

	public void setFeedbackAnalysisCategorySetsInUse(
			List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse) {
		this.feedbackAnalysisCategorySetsInUse = feedbackAnalysisCategorySetsInUse;
	}

	public FeedbackAnalyzationManagedBean() {

	}

	/**
	 * Initializes all the necessary information for the analyzation
	 */
	@PostConstruct
	public void init() {
		setCurrentRecordNumber(1);
		setNumberOfRecords(1);
		this.feedbackAnalyzationEntity = new FeedbackAnalyzationEntity();
		this.feedbackAnalyzationEntity.setCreated();
		feedbackAnalyzationEntity.setRecords(new ArrayList<FeedbackAnalysisRecordEntity>());
		currentRecord = new FeedbackAnalysisRecordEntity();
		if (feedbackAnalysisCategorySetsInUse != null)
			for (FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
				for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
					((FeedbackAnalysisCategoryEntity) fac).setInRecord(false);
		currentRecord.setFeedbackAnalyzation(feedbackAnalyzationEntity);
		feedbackAnalyzationEntity.addRecord(currentRecord);
	}

	public void setEventEntity(EventEntity eventEntity) {
		this.eventEntity = eventEntity;
	}

	public EventEntity getEventEntity() {
		return this.eventEntity;
	}

	public FeedbackAnalyzationEntity getFeedbackAnalyzationEntity() {
		return feedbackAnalyzationEntity;
	}

	public void setFeedbackAnalyzationEntity(FeedbackAnalyzationEntity feedbackAnalyzationEntity) {
		this.feedbackAnalyzationEntity = feedbackAnalyzationEntity;
	}

	public void setFeedbackAnalyzationName(String name) {
		this.feedbackAnalyzationEntity.setName(name);
	}

	public void setFeedbackAnalyzationDuration(long duration) {
		this.feedbackAnalyzationEntity.setDuration(duration);
	}

	/**
	 * saves the changes to the record currently in view and initializes a new
	 * record
	 */
	public void addRecord() {
		if (feedbackAnalyzationEntity == null)
			feedbackAnalyzationEntity = new FeedbackAnalyzationEntity();

		editRecord();

		comment = "";
		currentRecord = new FeedbackAnalysisRecordEntity();
		currentRecord.setFeedbackAnalyzation(feedbackAnalyzationEntity);
		feedbackAnalyzationEntity.addRecord(currentRecord);
		currentRecord.setSelectedCategories(new ArrayList<FeedbackAnalysisCategoryEntity>());

		numberOfRecords++;
		currentRecordNumber++;
	}

	/**
	 * saves the changes to the record currently in view
	 */
	public void editRecord() {

		List<FeedbackAnalysisCategoryEntity> selectedCategories = new ArrayList<FeedbackAnalysisCategoryEntity>();
		for (FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
			for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
				if (((FeedbackAnalysisCategoryEntity) fac).getInRecord()) {
					selectedCategories.add((FeedbackAnalysisCategoryEntity) fac);
					((FeedbackAnalysisCategoryEntity) fac).setInRecord(false);
				}

		currentRecord.setSelectedCategories(selectedCategories);

		currentRecord.setComment(comment);
	}

	public String toSummary() {
		editRecord();
		for (FeedbackAnalysisCategoryEntity cat: currentRecord.getSelectedCategories())
			cat.setInRecord(true);
		return "summary";
	}

	/**
	 * The method saves the analyzation to the database.
	 */
	public void saveFeedbackAnalyzation() {
		if (feedbackAnalyzationEntity.getId() == null) {
			feedbackAnalyzationEntity.setUserWantsToSaveToDatabase(true);

			for (FeedbackAnalysisCategorySetEntity categorySet : feedbackAnalysisCategorySetsInUse) {

				if (categorySet.getId() != null)
					categorySetEJB.detachCategorySet(categorySet);

				for (AbstractCategoryEntity cat : categorySet.getCategoryEntitys().values()) {
					LabelEntity label = labelEJB.findByLabel(cat.getLabel().getText());
					cat.setCategorySet(categorySet);
					if (label == null)
						labelEJB.create(cat.getLabel());
					else
						cat.setLabel(label);
				}

				categorySetEJB.create(categorySet);
			}
			feedbackAnalyzationEntity.setName("Analyzation - " + feedbackAnalyzationEntity.getCreated().toString());
			feedbackAnalyzationEntity.setEvent(eventEntity);
			feedbackAnalyzationEJB.create(feedbackAnalyzationEntity);
			feedbackAnalysisCategorySetsInUse=feedbackAnalyzationEntity.getFeedbackAnalysisCategorySets();
		} else {
			for (FeedbackAnalysisRecordEntity record:feedbackAnalyzationEntity.getRecords())
				if(record.getId()==null)feedbackAnalysisRecordEJB.create(record);
			feedbackAnalyzationEJB.edit(feedbackAnalyzationEntity);
		}
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public int getCurrentRecordNumber() {
		return currentRecordNumber;
	}

	public void setCurrentRecordNumber(int currentRecordNumber) {
		this.currentRecordNumber = currentRecordNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void resetCategorySetsInUse() {
		this.feedbackAnalysisCategorySetsInUse = null;
	}

}
