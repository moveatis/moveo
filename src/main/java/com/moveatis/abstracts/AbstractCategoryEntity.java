package com.moveatis.abstracts;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.moveatis.abstracts.BaseEntity;
import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.feedbackanalysiscategory.FeedbackAnalysisCategorySetEntity;
import com.moveatis.label.LabelEntity;
/**
 * Abstracts the categories to allow for categories in simple observations and in feedback analysis
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@MappedSuperclass
public abstract class AbstractCategoryEntity extends BaseEntity {
	 
    @ManyToOne(fetch=EAGER, cascade={PERSIST, MERGE})
    private LabelEntity label;
    
    private String description;
    
    
    private Integer orderNumber;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public LabelEntity getLabel() {
        return label;
    }

    public void setLabel(LabelEntity label) {
        this.label = label;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CategoryEntity;
    }

    @Override
    public String toString() {
        return "com.moveatis.category.Category[ id=" + id + " ]";
    }

	public abstract void setCategorySet(AbstractCategorySetEntity abstractCategorySetEntity);

	public abstract AbstractCategorySetEntity getCategorySet(); 




	
}
