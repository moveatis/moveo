package com.moveatis.user;

import com.moveatis.groupkey.GroupKeyEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TagUserEntity.class)
public abstract class TagUserEntity_ extends com.moveatis.user.AbstractUser_ {

	public static volatile SingularAttribute<TagUserEntity, IdentifiedUserEntity> creator;
	public static volatile SingularAttribute<TagUserEntity, String> label;
	public static volatile SingularAttribute<TagUserEntity, GroupKeyEntity> groupKey;

}

