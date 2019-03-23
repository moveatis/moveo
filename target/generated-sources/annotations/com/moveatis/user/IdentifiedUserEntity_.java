package com.moveatis.user;

import com.moveatis.identityprovider.IdentityProviderInformationEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IdentifiedUserEntity.class)
public abstract class IdentifiedUserEntity_ extends com.moveatis.user.AbstractUser_ {

	public static volatile SingularAttribute<IdentifiedUserEntity, String> givenName;
	public static volatile SingularAttribute<IdentifiedUserEntity, IdentityProviderInformationEntity> identityProviderInformation;
	public static volatile SingularAttribute<IdentifiedUserEntity, String> email;

}

