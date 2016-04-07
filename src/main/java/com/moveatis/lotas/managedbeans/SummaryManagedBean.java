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
import com.moveatis.lotas.managedbeans.CategoryManagedBean.CategorySet;
import com.moveatis.lotas.records.RecordEntity;
import com.moveatis.lotas.timezone.TimeZoneInformation;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
    private Locale locale;
    private TimeZone timeZone;
    private TimeZone browserTimeZone;
    private final Date min;
    private final Date start;
    private final long zoomMin;
    private final long zoomMax;
    private Date max;

    @EJB
    private Observation observationBean;

    @Inject
    private CategoryManagedBean categoryBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(SummaryManagedBean.class);

    /**
     * Default constructor to initialize timeline options.
     */
    public SummaryManagedBean() {
        this.locale = new Locale("fi", "FI"); // get users locale from session bean ?
        this.timeZone = TimeZoneInformation.getTimeZone(); // this is the servers timezone
        this.browserTimeZone = TimeZone.getTimeZone("Europe/Helsinki"); // get users browser timezone from session bean ?
        this.start = new Date(0);
        this.min = new Date(0);
        this.zoomMin = 10 * 1000;
        this.zoomMax = 24 * 60 * 60 * 1000;
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
     * Get timeline locale.
     *
     * @return Locale
     */
    public Locale getLocale() {
        return locale;  
    }  

    /**
     * Set timeline locale.
     *
     * @param locale Locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;  
    }

    /**
     * Get timeline time zone.
     *
     * @return TimeZone
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Set timeline time zone.
     *
     * @param timeZone TimeZone
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
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
     * Set user time zone for timeline.
     *
     * @param browserTimeZone
     */
    public void setBrowserTimeZone(TimeZone browserTimeZone) {
        this.browserTimeZone = browserTimeZone;
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
        //return observationBean.getName();
        Date date = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, this.locale);
        return "Observointi - " + df.format(date);
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
        return observationBean.getDuration();
    }

    /**
     * Create timeline model. Add category groups as timeline event groups and
     * records as timeline events.
     */
    private void createTimeline() {
        timeline = new TimelineModel();

        List<RecordEntity> records = observationBean.getRecords();
        List<CategorySet> categorySets = categoryBean.getCategorySets();

        // Add categories to timeline as timelinegroups
        int index = 0;
        for (CategorySet categorySet : categorySets) {
            for (String category : categorySet.getSelectedCategories()) {
                index++;
                // Add category name inside element with class name
                // use css style to hide them in timeline
                String numberedLabel = "<span class=categoryNumber>" + index + ". </span>"
                        + "<span class=categoryLabel>" + category + "</span>";
                TimelineGroup timelineGroup = new TimelineGroup(category, numberedLabel);
                timeline.addGroup(timelineGroup);
                // Add dummy records to show empty categories in timeline
                TimelineEvent timelineEvent = new TimelineEvent("", new Date(0), false, category, "dummyRecord");
                timeline.add(timelineEvent);
            }
        }

        // Add records to timeline as timeline-events
        for (RecordEntity record : records) {
            String category = record.getCategory();
            long startTime = record.getStartTime();
            long endTime = record.getEndTime();
            TimelineEvent timelineEvent = new TimelineEvent("", new Date(startTime),
                    new Date(endTime), false, category);
            timeline.add(timelineEvent);
        }
    }
}
