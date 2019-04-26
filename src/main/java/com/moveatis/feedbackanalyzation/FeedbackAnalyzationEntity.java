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
package com.moveatis.feedbackanalyzation;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.moveatis.abstracts.AbstractObservationEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;

/**
 * The entity for the feedback analyzation, corresponds to the
 * feedbackanalyzation-table in the database
 * 
 * @author Visa Nykänen
 */
@Table(name = "FEEDBACKANALYZATION")
@NamedQueries({
		@NamedQuery(name = "findFeedbackAnalyzationsByObserver", query = "SELECT observation FROM FeedbackAnalyzationEntity observation WHERE observation.observer=:observer"),
		@NamedQuery(name = "findFeedbackAnalyzationsWithoutEvent", query = "SELECT observation FROM FeedbackAnalyzationEntity observation WHERE observation.observer=:observer AND observation.event is null"),
		@NamedQuery(name = "findFeedbackAnalyzationsByEventsNotOwned", query = "SELECT observation FROM FeedbackAnalyzationEntity observation WHERE observation.observer=:observer AND observation.event.creator<>:observer") })
@Entity
public class FeedbackAnalyzationEntity extends AbstractObservationEntity {

	@OneToMany(mappedBy = "feedbackAnalyzation", fetch = FetchType.LAZY, cascade = ALL)
	private List<FeedbackAnalysisRecordEntity> records;

	public List<FeedbackAnalysisRecordEntity> getRecords() {
		return records;
	}

	public void setRecords(List<FeedbackAnalysisRecordEntity> records) {
		this.records = records;
	}

	public void addRecord(FeedbackAnalysisRecordEntity record) {
		if (this.getRecords() == null) {
			this.records = new ArrayList<>();
		}
		getRecords().add(record);
		record.setFeedbackAnalyzation(this);
	}

	/**
	 * The feedbackanalysiscategorysets for the feedback analyzation aren't saved so
	 * they have to be fetched by getting the categorysets to which all the selected
	 * categories belong to This means that if an analyzation has a categoryset that
	 * is not used in the analyzation, it won't be accessible when loading the
	 * analyzation later.
	 * 
	 * @return the feedbackanalysiscategorysets used by the analyzation
	 */
	public List<FeedbackAnalysisCategorySetEntity> getFeedbackAnalysisCategorySets() {
		List<FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySetsInUse = new ArrayList<FeedbackAnalysisCategorySetEntity>();
		for (FeedbackAnalysisRecordEntity record : records) {
			for (FeedbackAnalysisCategoryEntity cat : record.getSelectedCategories()) {
				boolean contained=false;
				for(FeedbackAnalysisCategorySetEntity facs:feedbackAnalysisCategorySetsInUse){
						contained=facs.getId()==cat.getCategorySet().getId();
						if(contained) break;						
					}
				if(!contained)feedbackAnalysisCategorySetsInUse.add(cat.getCategorySet());
			}
		}
		return feedbackAnalysisCategorySetsInUse;
	}
}
