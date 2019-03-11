package com.moveatis.groupkey;

import com.moveatis.event.EventGroupEntity;
import com.moveatis.user.IdentifiedUserEntity;
import com.moveatis.user.TagUserEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GroupKeyEntity.class)
public abstract class GroupKeyEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile SingularAttribute<GroupKeyEntity, IdentifiedUserEntity> creator;
	public static volatile SingularAttribute<GroupKeyEntity, TagUserEntity> tagUser;
	public static volatile SingularAttribute<GroupKeyEntity, String> label;
	public static volatile SingularAttribute<GroupKeyEntity, EventGroupEntity> eventGroup;
	public static volatile SingularAttribute<GroupKeyEntity, String> groupKey;

}

