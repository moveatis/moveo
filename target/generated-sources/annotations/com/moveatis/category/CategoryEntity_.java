package com.moveatis.category;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CategoryEntity.class)
public abstract class CategoryEntity_ extends com.moveatis.abstracts.AbstractCategoryEntity_ {

	public static volatile SingularAttribute<CategoryEntity, CategoryType> categoryType;
	public static volatile SingularAttribute<CategoryEntity, Boolean> canOverlap;
	public static volatile SingularAttribute<CategoryEntity, CategorySetEntity> categorySet;

}

