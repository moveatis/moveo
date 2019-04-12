package com.moveatis.identityprovider;

import com.moveatis.user.IdentifiedUserEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IdentityProviderInformationEntity.class)
public abstract class IdentityProviderInformationEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile SingularAttribute<IdentityProviderInformationEntity, IdentifiedUserEntity> userEntity;
	public static volatile SingularAttribute<IdentityProviderInformationEntity, String> affiliation;
	public static volatile SingularAttribute<IdentityProviderInformationEntity, String> username;

}

