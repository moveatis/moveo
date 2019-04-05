package com.moveatis.event;

import com.moveatis.feedbackanalyzation.FeedbackAnalyzationEntity;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.user.AbstractUser;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EventEntity.class)
public abstract class EventEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile SetAttribute<EventEntity, FeedbackAnalyzationEntity> analyzations;
	public static volatile SingularAttribute<EventEntity, AbstractUser> creator;
	public static volatile SetAttribute<EventEntity, ObservationEntity> observations;
	public static volatile SingularAttribute<EventEntity, String> description;
	public static volatile SingularAttribute<EventEntity, String> label;
	public static volatile SingularAttribute<EventEntity, EventGroupEntity> eventGroup;

}

