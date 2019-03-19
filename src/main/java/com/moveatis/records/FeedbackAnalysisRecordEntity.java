package com.moveatis.records;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import com.moveatis.abstracts.AbstractRecordEntity;
import com.moveatis.observation.ObservationCategorySet;


@Entity
@Table(name="FEEDBACKANALYSISRECORD")
public class FeedbackAnalysisRecordEntity extends AbstractRecordEntity {
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<FeedbackAnalysisRecordSelectedCategory> SelectedCategories;
}
