/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.managedbeans.CategorySelectionManagedBean.CategorySet;
import com.moveatis.lotas.records.RecordEntity;
import com.moveatis.lotas.timezone.TimeZoneInformation;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineGroup;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for Summary page managed bean. Responsive for creating summary page
 * timeline model and get required variables from observation.
 *
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@Named(value = "summaryBean")
@RequestScoped
public class SummaryManagedBean {
    
    private TimelineModel timeline;
    private final TimeZone serverTimeZone;
    private final TimeZone browserTimeZone;
    private final Date min;
    private final Date start;
    private final long zoomMin;
    private final long zoomMax;
    private Date max;
    private String observationName;
    private String observationDescription;
    private String observationTarget;
    private long observationDuration;

    @Inject
    private Observation observationEJB; //EJB-beans have EJB in their name by convention

    @Inject
    private CategorySelectionManagedBean categoryBean; //Managedbeans have Bean in their name by convention
    
    @Inject
    private Session sessionBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(SummaryManagedBean.class);

    /**
     * Default constructor to initialize timeline options.
     */
    public SummaryManagedBean() {
        this.serverTimeZone = TimeZoneInformation.getTimeZone(); // this is the servers timezone
        this.browserTimeZone = TimeZone.getTimeZone("Europe/Helsinki"); // get users browser timezone from session bean ?
        this.start = new Date(0);
        this.min = new Date(0);
        this.max = new Date(0);
        this.zoomMin = 10 * 1000;
        this.zoomMax = 24 * 60 * 60 * 1000;
        this.observationName = "";
    }

    /**
     * Post constructor to create timeline on request.
     */
    @PostConstruct
    protected void initialize() {
        createTimeline();
    }

    /**
     * Get timeline model.
     *
     * @return TimelineModel
     */
    public TimelineModel getTimeline() {
        return timeline;
    }

    /**
     * Get timeline time zone.
     *
     * @return TimeZone
     */
    public TimeZone getServerTimeZone() {
        return serverTimeZone;
    }

    /**
     * Get user time zone for timeline.
     *
     * @return TimeZone
     */
    public TimeZone getBrowserTimeZone() {
        return browserTimeZone;
    }

    /**
     * Get min date of timeline. User cannot move the timeline before that date.
     *
     * @return Date
     */
    public Date getMin() {
        return min;
    }

    /**
     * Get maximum date of timeline. User cannot move the timeline after that
     * date.
     *
     * @return Date
     */
    public Date getMax() {
        return max;
    }

    /**
     * Get timeline start date.
     *
     * @return Date
     */
    public Date getStart() {
        return start;
    }

    /**
     * Get minimum zoom interval for timeline in milliseconds.
     *
     * @return long
     */
    public long getZoomMin() {
        return zoomMin;
    }

    /**
     * Get maximum zoom interval for timeline in milliseconds.
     *
     * @return long
     */
    public long getZoomMax() {
        return zoomMax;
    }

    /**
     * Get observation name.
     *
     * @return String
     */
    public String getObservationName() {
        return observationName;
    }

    /**
     * Set observation name.
     * @param name observation name
     */
    public void setObservationName(String name) {
        this.observationName = name;
    }

    /**
     * Get observation target
     *
     * @return String
     */
    public String getObservationTarget() {
        //return observationBean.getTarget();
        return "";
    }

    /**
     * Get observation description
     *
     * @return String
     */
    public String getObservationDescription() {
        //return observationBean.getDescription();
        return "";
    }

    /**
     * Get observation duration
     *
     * @return long
     */
    public long getObservationDuration() {
        return observationDuration;
    }

    /**
     * Create timeline model. Add category groups as timeline event groups and
     * records as timeline events.
     */
    private void createTimeline() {
        timeline = new TimelineModel();

        SortedSet<Long> observations = sessionBean.getSessionObservationsIds();
        
        if(observations.isEmpty()) {
            return;
        }
        
        /*
        * TODO: SortedSet offers first-method to return first element in the set
        * What if there are more observations?
        */
        Long observationId = observations.first();

        setObservationName(observationEJB.find(observationId).getName());
        
        List<RecordEntity> records = observationEJB.findRecords(observationId);
        LOGGER.debug("Records-size ->" + records.size());
        List<CategorySet> categorySets = categoryBean.getCategorySetsInUse();

        this.observationDuration = observationEJB.find(observationId).getDuration();
        this.max = new Date(Math.round(this.observationDuration * 1.1)); // timeline max 110% of obs. duration

        // Add categories to timeline as timelinegroups
        int categoryNumber = 1;
        for (CategorySet categorySet : categorySets) {
            for (String category : categorySet.getSelectedCategories()) {
                // Add category name inside element with class name
                // use css style to hide them in timeline
                // class name is intentionally without quotes, timeline expectional case
                // TODO: escape XSS
                String numberedLabel = "<span class=categoryNumber>" + categoryNumber + ". </span>"
                        + "<span class=categoryLabel>" + category + "</span>";
                TimelineGroup timelineGroup = new TimelineGroup(category, numberedLabel);
                timeline.addGroup(timelineGroup);
                // Add dummy records to show empty categories in timeline
                TimelineEvent timelineEvent = new TimelineEvent("", new Date(0), false, category, "dummyRecord");
                timeline.add(timelineEvent);
                categoryNumber++;
            }
        }

        // Add records to timeline as timeline-events
        for (RecordEntity record : records) {
            String category = record.getCategory().getLabel().getLabel();
            long startTime = record.getStartTime();
            long endTime = record.getEndTime();
            TimelineEvent timelineEvent = new TimelineEvent("", new Date(startTime),
                    new Date(endTime), false, category);
            timeline.add(timelineEvent);
        }
    }
}
