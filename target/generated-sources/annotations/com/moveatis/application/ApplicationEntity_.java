package com.moveatis.application;

import com.moveatis.roles.SuperUserRoleEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ApplicationEntity.class)
public abstract class ApplicationEntity_ extends com.moveatis.abstracts.BaseEntity_ {

	public static volatile ListAttribute<ApplicationEntity, SuperUserRoleEntity> superUsers;
	public static volatile SingularAttribute<ApplicationEntity, String> reportEmail;
	public static volatile SingularAttribute<ApplicationEntity, Date> applicationInstalled;

}

