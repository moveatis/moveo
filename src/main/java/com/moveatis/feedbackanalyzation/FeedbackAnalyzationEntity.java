package com.moveatis.feedbackanalyzation;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moveatis.abstracts.AbstractObservationEntity;


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

}
