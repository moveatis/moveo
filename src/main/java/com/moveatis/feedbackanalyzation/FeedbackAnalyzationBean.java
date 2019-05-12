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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moveatis.abstracts.AbstractBean;
import com.moveatis.interfaces.Event;
import com.moveatis.interfaces.FeedbackAnalysisRecord;
import com.moveatis.interfaces.FeedbackAnalyzation;
import com.moveatis.observation.ObservationBean;
import com.moveatis.records.FeedbackAnalysisRecordEntity;
import com.moveatis.session.SessionBean;
import com.moveatis.user.AbstractUser;

/**
 * Manages the database connection for the feedback analyzations
 * 
 * @author Visa Nykänen
 */
@Stateful
public class FeedbackAnalyzationBean extends AbstractBean<FeedbackAnalyzationEntity>
		implements FeedbackAnalyzation, Serializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ObservationBean.class);

	private static final long serialVersionUID = 1L;

	@Inject
	private SessionBean sessionBean;

	@EJB
	private Event eventEJB;

	private FeedbackAnalyzationEntity feedbackAnalyzationEntity;

	@PersistenceContext(unitName = "MOVEATIS_PERSISTENCE")
	private EntityManager em;

	@Inject
	private FeedbackAnalysisRecord feedbackAnalysisRecordEJB;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public FeedbackAnalyzationBean() {
		super(FeedbackAnalyzationEntity.class);
	}

	/**
	 * Finds and returns all observations for the specific user.
	 * 
	 * @param observer
	 *            The user, whose analyzations are to be searched.
	 * @return A list of the analyzations for the user.
	 */
	@Override
	public List<FeedbackAnalyzationEntity> findAllByObserver(AbstractUser observer) {
		TypedQuery<FeedbackAnalyzationEntity> query = em.createNamedQuery("findFeedbackAnalyzationsByObserver",
				FeedbackAnalyzationEntity.class);
		query.setParameter("observer", observer);
		return query.getResultList();
	}

	/**
	 * Finds the analyzations for the user, which have no event attached to them.
	 * 
	 * @param observer
	 *            The user, whose analyzations are to be searched.
	 * @return A list of the analyzations.
	 */
	@Override
	public List<FeedbackAnalyzationEntity> findWithoutEvent(AbstractUser observer) {
		TypedQuery<FeedbackAnalyzationEntity> query = em.createNamedQuery("findFeedbackAnalyzationsWithoutEvent",
				FeedbackAnalyzationEntity.class);
		query.setParameter("observer", observer);
		return query.getResultList();
	}

	/**
	 * Finds the analyzatinos that are made for events that the specified user does
	 * not own.
	 * 
	 * @param observer
	 *            The user, whose analyzations are to be searched.
	 * @return A list of the analyzations.
	 */
	@Override
	public List<FeedbackAnalyzationEntity> findByEventsNotOwned(AbstractUser observer) {
		TypedQuery<FeedbackAnalyzationEntity> query = em.createNamedQuery("findFeedbackAnalyzationsByEventsNotOwned",
				FeedbackAnalyzationEntity.class);
		query.setParameter("observer", observer);
		return query.getResultList();
	}

	/**
	 * Persists the observations to the database.
	 * 
	 * @param feedbackAnalyzation
	 *            The observatio entity to be persisted.
	 */
	@Override
	public void create(FeedbackAnalyzationEntity feedbackAnalyzation) {
		super.create(feedbackAnalyzation);
	}

	@Override
	public void edit(FeedbackAnalyzationEntity feedbackAnalyzation) {
		super.edit(feedbackAnalyzation);
	}

	/**
	 * Finds a list of the records for the observation with the given id.
	 * 
	 * @param id
	 *            The id of the analyzation.
	 * @return A list of the records.
	 */
	@Override
	public List<FeedbackAnalysisRecordEntity> findRecords(Object id) {
		feedbackAnalyzationEntity = em.find(FeedbackAnalyzationEntity.class, id);
		if (feedbackAnalyzationEntity != null) {
			return feedbackAnalyzationEntity.getRecords();
		}
		return new ArrayList<>(); // return empty list
	}

	/**
	 * Removes the analyzation and also removes the analyzation from the event it
	 * was associated with.
	 * 
	 * @param feedbackAnalyzationEntity
	 *            The analyzation to be removed.
	 */
	@Override
	public void remove(FeedbackAnalyzationEntity feedbackAnalyzationEntity) {
		super.remove(feedbackAnalyzationEntity);
		eventEJB.removeFeedbackAnalyzation(feedbackAnalyzationEntity);
		feedbackAnalyzationEntity.setEvent(null);
		super.edit(feedbackAnalyzationEntity);
	}

	@Override
	public void removeRecordFromAnalyzation(FeedbackAnalyzationEntity feedbackAnalyzation,
			FeedbackAnalysisRecordEntity record) {
		List<FeedbackAnalysisRecordEntity> records = feedbackAnalyzation.getRecords();
		records.remove(record);
		record.setFeedbackAnalyzation(null);
		feedbackAnalyzation.setRecords(records);
		feedbackAnalysisRecordEJB.remove(record);
		for (FeedbackAnalysisRecordEntity rec : feedbackAnalyzation.getRecords()) {
			if (rec.getId() == null)
				feedbackAnalysisRecordEJB.create(rec);
			else
				feedbackAnalysisRecordEJB.edit(rec);
		}
		super.edit(feedbackAnalyzation);
	}

	/**
	 * Permanently removes the analyzation, which the user did not set to be saved
	 * into the database.
	 * 
	 * @param feedbackAnalyzationEntity
	 *            The analyzation to be removed.
	 */
	@Override
	public void removeUnsavedObservation(FeedbackAnalyzationEntity feedbackAnalyzationEntity) {
		em.remove(em.merge(feedbackAnalyzationEntity));
	}
}
