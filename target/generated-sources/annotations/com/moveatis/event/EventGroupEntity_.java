package com.moveatis.event;

import com.moveatis.category.CategorySetEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.groupkey.GroupKeyEntity;
import com.moveatis.user.AbstractUser;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EventGroupEntity.class)
public abstract class EventGroupEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile ListAttribute<EventGroupEntity, FeedbackAnalysisCategorySetEntity> feedbackAnalysisCategorySets;
	public static volatile SingularAttribute<EventGroupEntity, AbstractUser> owner;
	public static volatile SetAttribute<EventGroupEntity, CategorySetEntity> categorySets;
	public static volatile SingularAttribute<EventGroupEntity, String> description;
	public static volatile SingularAttribute<EventGroupEntity, String> label;
	public static volatile SingularAttribute<EventGroupEntity, EventEntity> event;
	public static volatile SingularAttribute<EventGroupEntity, GroupKeyEntity> groupKey;
	public static volatile SetAttribute<EventGroupEntity, AbstractUser> users;

}

