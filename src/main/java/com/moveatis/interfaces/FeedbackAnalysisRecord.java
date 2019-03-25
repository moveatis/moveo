package com.moveatis.interfaces;

import java.util.List;

import com.moveatis.records.FeedbackAnalysisRecordEntity;

public interface FeedbackAnalysisRecord {
    void create(FeedbackAnalysisRecordEntity feedbackAnalysisRecordEntity);

    void edit(FeedbackAnalysisRecordEntity feedbackAnalysisRecordEntity);

    void remove(FeedbackAnalysisRecordEntity feedbackAnalysisRecordEntity);

    FeedbackAnalysisRecordEntity find(Object id);

    List<FeedbackAnalysisRecordEntity> findAll();

    List<FeedbackAnalysisRecordEntity> findRange(int[] range);

    int count();
}
