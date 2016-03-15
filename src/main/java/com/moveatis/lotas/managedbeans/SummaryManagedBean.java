/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.records.RecordEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineGroup;
import org.primefaces.extensions.model.timeline.TimelineModel;

/**
 *
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@RequestScoped
@Named(value = "summaryBean")
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
    private String observationDate;
    private String observationDuration;
    
    @EJB
    private Observation observationBean;

    public SummaryManagedBean() {
        this.locale = new Locale("fi", "FI"); // get from locale "bean" ?
        this.timeZone = TimeZone.getTimeZone("UTC"); // get from timezone "bean" ?
        this.browserTimeZone = TimeZone.getTimeZone("Europe/Helsinki"); // get from timezone "bean" ?
        this.start = new Date(0);
        this.min = new Date(0);
        this.zoomMin = 10 * 1000;
        this.zoomMax = 24 * 60 * 60 * 1000;
    }

    @PostConstruct
    protected void initialize() {
        createTimeline();
    }

    public TimelineModel getTimeline() {
        return timeline;
    }  

    public Locale getLocale() {
        return locale;  
    }  

    public void setLocale(Locale locale) {
        this.locale = locale;  
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public TimeZone getBrowserTimeZone() {
        return browserTimeZone;
    }

    public void setBrowserTimeZone(TimeZone browserTimeZone) {
        this.browserTimeZone = browserTimeZone;
    }

    public Date getMin() {
        return min;
    }

    public Date getMax() {
        return max;
    }

    public Date getStart() {
        return start;
    }

    public long getZoomMin() {
        return zoomMin;
    }

    public long getZoomMax() {
        return zoomMax;
    }

    public String getObservationDate() {
        return observationDate;
    }

    public String getObservationDuration() {
        return observationDuration;
    }

    private void createTimeline() {
        timeline = new TimelineModel();
        HashSet<String> categories = new HashSet<>();
        for (RecordEntity record : observationBean.getRecords()) {
            String category = record.getCategory();
            Date recordStart = new Date(record.getStartTime());
            Date recordEnd = new Date(record.getEndTime());
            TimelineEvent timelineEvent = new TimelineEvent("", recordStart, recordEnd, true, category);
            if (!categories.contains(category)) {
                timeline.addGroup(new TimelineGroup(category, category));
                categories.add(category);
            }
            timeline.add(timelineEvent);
        }
    }
}
