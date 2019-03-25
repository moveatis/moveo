package com.moveatis.observation;

import com.moveatis.records.RecordEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ObservationEntity.class)
public abstract class ObservationEntity_ extends com.moveatis.abstracts.AbstractObservationEntity_ {

	public static volatile SetAttribute<ObservationEntity, ObservationCategorySet> observationCategorySets;
	public static volatile ListAttribute<ObservationEntity, RecordEntity> records;

}

