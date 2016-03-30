package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.records.RecordEntity;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

    public String getObservationName() {
        //return observationBean.getName();
        Date date = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, this.locale);
        return "Observointi - " + df.format(date);
    }

    public String getObservationTarget() {
        //return observationBean.getTarget();
        return "";
    }

    public String getObservationDescription() {
        //return observationBean.getDescription();
        return "";
    }

    public String getObservationDuration() {
        // return observationBean.getDuration();
        return "hh:mm:ss";
    }

    private void createTimeline() {
        timeline = new TimelineModel();

        List<RecordEntity> records = observationBean.getRecords();
        //List<CategoryEntity> categories = observationBean.getCategories();

        HashSet<String> categories = new HashSet<>();

        // Add categories to timeline as timelinegroups
        // TODO: Get order of categories from observationBean.
        //// Now categories follow the order of records.
        for (RecordEntity record : records) {
            String category = record.getCategory().getLabel();
            if (!categories.contains(category)) {
                categories.add(record.getCategory().getLabel());
                // Add category name inside element with class name
                // use css style to hide them in timeline
                String numberedLabel = "<span class=categoryNumber>" + categories.size() + ". </span>"
                        + "<span class=categoryLabel>" + category + "</span>";
                TimelineGroup timelineGroup = new TimelineGroup(category, numberedLabel);
                timeline.addGroup(timelineGroup);
            }
        }

        // Add records to timeline as timeline-events
        for (RecordEntity record : records) {
            String category = record.getCategory().getLabel();
            long startTime = record.getStartTime();
            long endTime = record.getEndTime();
            TimelineEvent timelineEvent = new TimelineEvent("", new Date(startTime),
                    new Date(endTime), false, category);
            timeline.add(timelineEvent);
        }
    }
}
