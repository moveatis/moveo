package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.managedbeans.CategoryManagedBean.CategorySet;
import com.moveatis.lotas.records.RecordEntity;
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

    public SummaryManagedBean() {
        this.locale = new Locale("fi", "FI"); // get users locale from session bean ?
        this.timeZone = TimeZone.getTimeZone("UTC"); // this should be the servers timezone
        this.browserTimeZone = TimeZone.getTimeZone("Europe/Helsinki"); // get users browser timezone from session bean ?
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
        return "??h ??m ??s";
    }

    public long getEndTime() {
        return observationBean.getDuration();
    }

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
