package com.moveatis.label;

import com.moveatis.category.CategoryEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LabelEntity.class)
public abstract class LabelEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile ListAttribute<LabelEntity, FeedbackAnalysisCategoryEntity> feedbackAnalysisCategoryEntities;
	public static volatile ListAttribute<LabelEntity, CategoryEntity> categoryEntities;
	public static volatile SingularAttribute<LabelEntity, String> text;

}

