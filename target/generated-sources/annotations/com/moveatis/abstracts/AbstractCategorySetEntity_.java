package com.moveatis.abstracts;

import com.moveatis.event.EventGroupEntity;
import com.moveatis.user.IdentifiedUserEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractCategorySetEntity.class)
public abstract class AbstractCategorySetEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile SingularAttribute<AbstractCategorySetEntity, IdentifiedUserEntity> creator;
	public static volatile SingularAttribute<AbstractCategorySetEntity, EventGroupEntity> eventGroupEntity;
	public static volatile SingularAttribute<AbstractCategorySetEntity, String> description;
	public static volatile SingularAttribute<AbstractCategorySetEntity, String> label;

}

