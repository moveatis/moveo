package com.moveatis.records;

import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationEntity;
import java.io.File;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RecordEntity.class)
public abstract class RecordEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile SingularAttribute<RecordEntity, ObservationEntity> observation;
	public static volatile SingularAttribute<RecordEntity, Long> startTime;
	public static volatile SingularAttribute<RecordEntity, String> comment;
	public static volatile SingularAttribute<RecordEntity, File> voiceComment;
	public static volatile SingularAttribute<RecordEntity, Long> endTime;
	public static volatile SingularAttribute<RecordEntity, ObservationCategory> category;

}

