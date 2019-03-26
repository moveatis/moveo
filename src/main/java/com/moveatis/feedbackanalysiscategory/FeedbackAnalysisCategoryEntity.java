package com.moveatis.feedbackanalysiscategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.abstracts.AbstractCategorySetEntity;
import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.records.FeedbackAnalysisRecordEntity;
@Table(name="FEEDBACKANALYSISCATEGORY")
@Entity
public class FeedbackAnalysisCategoryEntity extends AbstractCategoryEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @ManyToOne
    private FeedbackAnalysisCategorySetEntity feedbackAnalysisCategorySet;
    
    @ManyToMany(mappedBy="selectedCategories")
    private List<FeedbackAnalysisRecordEntity> recordsContainingThisFeedbackAnalysisCategory;
	@Transient
    private boolean inRecord=false;
    
    public List<FeedbackAnalysisRecordEntity> getRecordsContainingThisFeedbackAnalysisCategory() {
		return recordsContainingThisFeedbackAnalysisCategory;
	}

	public void setRecordsContainingThisFeedbackAnalysisCategory(
		List<FeedbackAnalysisRecordEntity> recordsContainingThisFeedbackAnalysisCategory) {
		this.recordsContainingThisFeedbackAnalysisCategory=new ArrayList<FeedbackAnalysisRecordEntity>();
		for (FeedbackAnalysisRecordEntity far: recordsContainingThisFeedbackAnalysisCategory) {
			far.addSelectedCategory(this);
			this.recordsContainingThisFeedbackAnalysisCategory.add(far);
		}
	}
	public boolean getInRecord() {
		return inRecord;
	}
	public void setInRecord(boolean inRecord) {
		this.inRecord=inRecord;
	}
	@Override
    public FeedbackAnalysisCategorySetEntity getCategorySet() {
        return feedbackAnalysisCategorySet;
    }

    @Override
	public void setCategorySet(AbstractCategorySetEntity categorySetEntity) {
		this.feedbackAnalysisCategorySet=(FeedbackAnalysisCategorySetEntity)categorySetEntity;
	}
    
    

}
