package com.moveatis.records;

import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationEntity;
import java.io.File;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RecordEntity.class)
public abstract class RecordEntity_ extends com.moveatis.abstracts.AbstractRecordEntity_ {

	public static volatile SingularAttribute<RecordEntity, ObservationEntity> observation;
	public static volatile SingularAttribute<RecordEntity, File> voiceComment;
	public static volatile SingularAttribute<RecordEntity, ObservationCategory> category;

}

