package com.moveatis.feedbackanalysiscategory;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategoryEntity;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.abstracts.AbstractCategorySetEntity;
import com.moveatis.category.CategorySetEntity;

@Entity
@Table(name="FEEDBACKANALYSISCATEGORYSET")
public class FeedbackAnalysisCategorySetEntity extends AbstractCategorySetEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "feedbackAnalysisCategorySet", cascade = {PERSIST, MERGE}, fetch = FetchType.LAZY, targetEntity = FeedbackAnalysisCategoryEntity.class)
    @CollectionTable(name="FEEDBACKANALYSISCATEGORYENTITIES")
    @MapKey(name="orderNumber")
    @Column(name="FEEDBACKANALYSISCATEGORYENTITY_ORDERNUMBER")
    private Map<Integer, AbstractCategoryEntity> categoryEntitys;

    
    @Override
    public Map<Integer, AbstractCategoryEntity> getCategoryEntitys() {
        return categoryEntitys;
    }

    @Override
    public void setCategoryEntitys(Map<Integer, AbstractCategoryEntity> categories) {
        this.categoryEntitys = categories;
    }



    
}