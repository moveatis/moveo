package com.moveatis.feedbackanalysiscategory;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.abstracts.AbstractCategorySetEntity;
import com.moveatis.category.CategorySetEntity;
@Table(name="FEEDBACKANALYSISCATEGORY")
@Entity
@NamedQuery(name="FeedbackAnalysisCategory.findByLabel", query="SELECT category FROM FeedbackAnalysisCategoryEntity category WHERE category.label = :label")
public class FeedbackAnalysisCategoryEntity extends AbstractCategoryEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @ManyToOne
    private FeedbackAnalysisCategorySetEntity feedbackAnalysisCategorySet;
    
    @Override
    public FeedbackAnalysisCategorySetEntity getCategorySet() {
        return feedbackAnalysisCategorySet;
    }

    @Override
	public void setCategorySet(AbstractCategorySetEntity categorySetEntity) {
		this.feedbackAnalysisCategorySet=(FeedbackAnalysisCategorySetEntity)categorySetEntity;
	}

}
