package com.moveatis.observation;

import com.moveatis.event.EventEntity;
import com.moveatis.records.RecordEntity;
import com.moveatis.user.AbstractUser;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ObservationEntity.class)
public abstract class ObservationEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile SingularAttribute<ObservationEntity, Long> duration;
	public static volatile SingularAttribute<ObservationEntity, AbstractUser> observer;
	public static volatile SingularAttribute<ObservationEntity, Boolean> userWantsToSaveToDatabase;
	public static volatile SetAttribute<ObservationEntity, ObservationCategorySet> observationCategorySets;
	public static volatile ListAttribute<ObservationEntity, RecordEntity> records;
	public static volatile SingularAttribute<ObservationEntity, String> name;
	public static volatile SingularAttribute<ObservationEntity, String> description;
	public static volatile SingularAttribute<ObservationEntity, EventEntity> event;
	public static volatile SingularAttribute<ObservationEntity, String> target;

}

