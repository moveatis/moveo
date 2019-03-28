package com.moveatis.feedbackanalysiscategory;

import com.moveatis.records.FeedbackAnalysisRecordEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FeedbackAnalysisCategoryEntity.class)
public abstract class FeedbackAnalysisCategoryEntity_ extends com.moveatis.abstracts.AbstractCategoryEntity_ {

	public static volatile ListAttribute<FeedbackAnalysisCategoryEntity, FeedbackAnalysisRecordEntity> recordsContainingThisFeedbackAnalysisCategory;
	public static volatile SingularAttribute<FeedbackAnalysisCategoryEntity, FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySet;

}

