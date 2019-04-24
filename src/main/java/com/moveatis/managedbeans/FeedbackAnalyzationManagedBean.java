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

/**
 * The managed bean controlling the feedbackanalyzation in view TODO: extract
 * the methods concerning only a certain view to new managed beans controlling
 * said views (analyzer, summary, recordtable)
 * 
 * @author Visa Nykänen
 */
@Named(value = "feedbackAnalyzationManagedBean")
@SessionScoped
public class FeedbackAnalyzationManagedBean implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObservationManagedBean.class);

	private static final long serialVersionUID = 1L;

	@Inject
	private Label labelEJB;

	/**
	 * The feedbackanalyzationentity being edited
	 */
	private FeedbackAnalyzationEntity feedbackAnalyzationEntity;

	/**
	 * The categorysets being used in the analyzation event
	 */
	private List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse;

	/**
	 * The index(+1) of the record currently in view
	 */

	private int currentRecordNumber;

	/**
	 * If the analyzation has some new categorysets they need to be saved, so
	 * CategorySetBean is needed
	 */
	@Inject
	private CategorySet categorySetEJB;

	/**
	 * The record currently in view
	 */
	private FeedbackAnalysisRecordEntity currentRecord;

	/**
	 * The event the analyzation is performed for
	 */
	private EventEntity eventEntity;

	/**
	 * used to save the analyzation to the database
	 */
	@EJB
	private FeedbackAnalyzation feedbackAnalyzationEJB;

	/**
	 * The categorysets are gotten from the session
	 */
	@Inject
	private Session sessionBean;

	/**
	 * The comment for the record currently in view
	 */
	private String comment;

	private FeedbackAnalysisCategoryEntity selectedCategory;

	/**
	 * If new records are added to the analyzation after it has already been saved
	 * to the database the records need to be saved individually so
	 * feedbackanalysisrecordbean is needed
	 */
	@Inject
	private FeedbackAnalysisRecord feedbackAnalysisRecordEJB;

	/**
	 * The timer value, set to be the duration of the analyzation once navigating to
	 * the record table
	 */
	private long duration;

	/**
	 * Whether the timer is stopped
	 */
	private boolean isTimerStopped;

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

	public boolean getIsTimerStopped() {
		return isTimerStopped;
	}

	public void setIsTimerStopped(boolean timerStopped) {
		this.isTimerStopped = timerStopped;
	}

	public void pauseContinue() {
		isTimerStopped = !isTimerStopped;
	}

	public String getDurationAsString() {
		return getLongAsTimeStamp(duration);
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setSelectedCategory(FeedbackAnalysisCategoryEntity selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public FeedbackAnalysisCategoryEntity getSelectedCategory() {
		return selectedCategory;
	}

	public FeedbackAnalysisRecordEntity getCurrentRecord() {
		return currentRecord;
	}

	public List<FeedbackAnalysisCategorySetEntity> getFeedbackAnalysisCategorySetsInUse() {
		return feedbackAnalysisCategorySetsInUse;
	}

	public void setFeedbackAnalysisCategorySetsInUse(
			List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse) {
		this.feedbackAnalysisCategorySetsInUse = feedbackAnalysisCategorySetsInUse;
	}

	public long getMaxTimeStampForCurrentRecord() {
		if (currentRecordNumber == feedbackAnalyzationEntity.getRecords().size())
			return duration;
		for (int i = currentRecordNumber+1; i <= feedbackAnalyzationEntity.getRecords().size(); i++) {
			long start = findRecordByOrderNumber(i).getStartTime();
			if (start > 0)
				return start;
		}
		return duration;
	}

	public long getMinTimeStampForCurrentRecord() {
		if(currentRecordNumber==1) return 0;
		for(int i=currentRecordNumber-1; i>=1; i--) {
			long start=findRecordByOrderNumber(i).getStartTime();
			if(start>0) return start;	
			}
		return 0;
	}

	/**
	 * Returns the given number of seconds in a string showing the minutes and
	 * seconds
	 * 
	 * @param seconds the value as seconds
	 * @return the timestamp as the amount of minutes and seconds in a string
	 */
	public String getLongAsTimeStamp(long seconds) {
		if (seconds == 0)
			return "-- min -- s";
		return seconds / 60 + " min " + seconds % 60 + " s";

	}

	/**
	 * increments the timer value every second if the timer is running
	 */
	public void increment() {
		if (!isTimerStopped)
			duration += 1;
	}

	/**
	 * adds a category to the currently shown record
	 * 
	 * @param category the category to be added
	 */
	public void addCategoryToCurrentRecord(FeedbackAnalysisCategoryEntity category) {
		currentRecord.addSelectedCategory(category);
	}

	/**
	 * Sets the currently shown record to be the record given in the parameter
	 * 
	 * @param currentRecord the record to be shown
	 */
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

	/**
	 * Sets the starttime of the currently viewed record based on the timer value if
	 * the record isn't in between other records, its starttime hasn't already been
	 * set and the timer is running
	 */
	public void setTimeStamp() {
		if (currentRecord.getStartTime() == null && !isTimerStopped
				&& currentRecordNumber == feedbackAnalyzationEntity.getRecords().size())
			currentRecord.setStartTime(duration);
	}

	/**
	 * Finds the record in the feedbackanalyzation based on its ordernumber
	 * 
	 * @param orderNumber the ordernumber of the record to be accessed
	 * @return the record with the given ordernumber
	 */
	private FeedbackAnalysisRecordEntity findRecordByOrderNumber(Integer orderNumber) {
		List<FeedbackAnalysisRecordEntity> records = feedbackAnalyzationEntity.getRecords();
		for (FeedbackAnalysisRecordEntity record : records)
			if (record.getOrderNumber() == orderNumber)
				return record;
		return new FeedbackAnalysisRecordEntity();
	}

	/**
	 * Sets the ordernumber for the currently edited record If the record is added
	 * between records, sets the following records ordernumbers to be one higher
	 */
	private void setOrderNumberForRecord() {
		for (int i = currentRecordNumber; i <= feedbackAnalyzationEntity.getRecords().size(); i++)
			findRecordByOrderNumber(i).setOrderNumber(i + 1);
		currentRecord.setOrderNumber(currentRecordNumber);
	}

	/**
	 * Sets the record to be shown in the view based on the given ordernumber
	 * 
	 * @param recordNumber The ordernumber of the record to be accessed
	 */
	public void setCurrentRecord(int recordNumber) {
		if (recordNumber > feedbackAnalyzationEntity.getRecords().size() || recordNumber < 1
				|| recordNumber == currentRecordNumber)
			return;
		editRecord();
		currentRecordNumber = recordNumber;
		currentRecord = findRecordByOrderNumber(recordNumber);
		for (FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
			for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
				((FeedbackAnalysisCategoryEntity) fac).setInRecord(false);
		comment = currentRecord.getComment();

		List<FeedbackAnalysisCategoryEntity> selectedCategories = currentRecord.getSelectedCategories();
		for (FeedbackAnalysisCategoryEntity category : selectedCategories)
			category.setInRecord(true);
	}

	public FeedbackAnalyzationManagedBean() {

	}

	/**
	 * Initializes all the necessary information for the analyzation
	 */
	@PostConstruct
	public void init() {
		setCurrentRecordNumber(1);
		duration = 0;
		isTimerStopped = false;
		this.feedbackAnalyzationEntity = new FeedbackAnalyzationEntity();
		this.feedbackAnalyzationEntity.setCreated();
		feedbackAnalyzationEntity.setRecords(new ArrayList<FeedbackAnalysisRecordEntity>());
		currentRecord = new FeedbackAnalysisRecordEntity();
		setOrderNumberForRecord();
		if (feedbackAnalysisCategorySetsInUse != null)
			for (FeedbackAnalysisCategorySetEntity facs : feedbackAnalysisCategorySetsInUse)
				for (AbstractCategoryEntity fac : facs.getCategoryEntitys().values())
					((FeedbackAnalysisCategoryEntity) fac).setInRecord(false);
		currentRecord.setFeedbackAnalyzation(feedbackAnalyzationEntity);
		feedbackAnalyzationEntity.addRecord(currentRecord);
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
		currentRecord.setSelectedCategories(new ArrayList<FeedbackAnalysisCategoryEntity>());
		currentRecordNumber++;
		setOrderNumberForRecord();
		feedbackAnalyzationEntity.addRecord(currentRecord);
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

	/**
	 * navigates to the summary page
	 * 
	 * @return the key string that is used by facesconfig.xml to navigate to the
	 *         correct page
	 */
	public String toSummary() {
		return "summary";
	}

	/**
	 * Makes sure changes to the currently shown record are saved, stops the timer,
	 * sets the duration of the analyzation and navigates to the recordtable page
	 * 
	 * @return the key string that is used by facesconfig.xml to navigate to the
	 *         correct page
	 */
	public String toRecordTable() {
		editRecord();
		feedbackAnalyzationEntity.setDuration(duration);
		isTimerStopped = true;
		for (FeedbackAnalysisCategoryEntity cat : currentRecord.getSelectedCategories())
			cat.setInRecord(true);
		return "recordtable";
	}

	/**
	 * The method saves the analyzation to the database. Copies are made of all the
	 * categorysets used by the analyzation, so that later edits to the categorysets
	 * won't effect old analyzations
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
			feedbackAnalyzationEntity.setObserver(sessionBean.getLoggedIdentifiedUser());
			feedbackAnalyzationEJB.create(feedbackAnalyzationEntity);
		} else {
			for (FeedbackAnalysisRecordEntity record : feedbackAnalyzationEntity.getRecords())
				if (record.getId() == null)
					feedbackAnalysisRecordEJB.create(record);
			feedbackAnalyzationEJB.edit(feedbackAnalyzationEntity);
		}
	}

	/**
	 * Sets the categorysetsinuse to be null,
	 */
	public void resetCategorySetsInUse() {
		this.feedbackAnalysisCategorySetsInUse = null;
	}

}
