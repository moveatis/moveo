package com.moveatis.feedbackanalyzation;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.moveatis.abstracts.AbstractObservationEntity;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.records.FeedbackAnalysisRecordEntity;
import com.moveatis.records.RecordEntity;


@Table(name = "FEEDBACKANALYZATION")
@NamedQueries({
    @NamedQuery(
            name = "findFeedbackAnalyzationsByObserver",
            query = "SELECT observation FROM FeedbackAnalyzationEntity observation WHERE observation.observer=:observer"
    ),
    @NamedQuery(
            name = "findFeedbackAnalyzationsWithoutEvent",
            query = "SELECT observation FROM FeedbackAnalyzationEntity observation WHERE observation.observer=:observer AND observation.event is null"
    ),
    @NamedQuery(
            name = "findFeedbackAnalyzationsByEventsNotOwned",
            query = "SELECT observation FROM FeedbackAnalyzationEntity observation WHERE observation.observer=:observer AND observation.event.creator<>:observer"
    )
})
@Entity
public class FeedbackAnalyzationEntity extends AbstractObservationEntity {


    @OneToMany(mappedBy = "FeedbackAnalyzation", fetch = FetchType.LAZY, cascade = ALL)
    private List<FeedbackAnalysisRecordEntity> records;

    

    public List<FeedbackAnalysisRecordEntity> getRecords() {
        return records;
    }

    public void setRecords(List<FeedbackAnalysisRecordEntity> records) {
        this.records = records;
    }

    public void addRecord(FeedbackAnalysisRecordEntity record) {
        if (this.getRecords() == null) {
            this.records = new ArrayList<>();
        }
        getRecords().add(record);
        record.setFeedbackAnalyzation(this);
    }

}
