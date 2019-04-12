package com.moveatis.abstracts;

import com.moveatis.label.LabelEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractCategoryEntity.class)
public abstract class AbstractCategoryEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile SingularAttribute<AbstractCategoryEntity, Integer> orderNumber;
	public static volatile SingularAttribute<AbstractCategoryEntity, String> description;
	public static volatile SingularAttribute<AbstractCategoryEntity, LabelEntity> label;

}

