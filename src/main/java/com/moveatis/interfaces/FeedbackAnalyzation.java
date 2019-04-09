package com.moveatis.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;
import com.moveatis.records.RecordEntity;
import com.moveatis.user.AbstractUser;
@Local(FeedbackAnalyzation.class)
public interface FeedbackAnalyzation {
	  void create(FeedbackAnalyzationEntity feedbackAnalyzationEntity);

	    void edit(FeedbackAnalyzationEntity feedbackAnalyzationEntity);

	    void remove(FeedbackAnalyzationEntity feedbackAnalyzationEntity);
	    
	    void removeUnsavedObservation(FeedbackAnalyzationEntity feedbackAnalyzationEntity);
	    
	    FeedbackAnalyzationEntity find(Object id);

	    List<FeedbackAnalyzationEntity> findAll();

	    List<FeedbackAnalyzationEntity> findAllByObserver(AbstractUser analyzer);

	    List<FeedbackAnalyzationEntity> findWithoutEvent(AbstractUser analyzer);

	    List<FeedbackAnalyzationEntity> findByEventsNotOwned(AbstractUser analyzer);

	    List<FeedbackAnalyzationEntity> findRange(int[] range);
	    
	    List<FeedbackAnalysisRecordEntity> findRecords(Object id);

	    int count();

		void removeRecordFromAnalyzation(FeedbackAnalyzationEntity feedbackAnalyzation,FeedbackAnalysisRecordEntity record);
}
