package com.moveatis.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;
import com.moveatis.records.RecordEntity;
import com.moveatis.user.AbstractUser;
@Local(FeedbackAnalyzation.class)
public interface FeedbackAnalyzation {
	  void create(FeedbackAnalyzationEntity observationEntity);

	    void edit(FeedbackAnalyzationEntity observationEntity);

	    void remove(FeedbackAnalyzationEntity observationEntity);
	    
	    void removeUnsavedObservation(FeedbackAnalyzationEntity observationEntity);

	    FeedbackAnalyzationEntity find(Object id);

	    List<FeedbackAnalyzationEntity> findAll();

	    List<FeedbackAnalyzationEntity> findAllByObserver(AbstractUser observer);

	    List<FeedbackAnalyzationEntity> findWithoutEvent(AbstractUser observer);

	    List<FeedbackAnalyzationEntity> findByEventsNotOwned(AbstractUser observer);

	    List<FeedbackAnalyzationEntity> findRange(int[] range);
	    
	    List<FeedbackAnalysisRecordEntity> findRecords(Object id);

	    int count();
}
