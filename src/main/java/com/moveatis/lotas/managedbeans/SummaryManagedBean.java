/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.interfaces.DebugObservation;
import com.moveatis.lotas.restful.DebugRecordEntity;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineGroup;
import org.primefaces.extensions.model.timeline.TimelineModel;

/**
 *
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@ManagedBean
@ViewScoped  
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

    private Observation observation;
    
    @EJB(beanName="DebugObservationBean")
    private DebugObservation observationBean;
    
//    @ManagedProperty(value = "#{observationBean}")
    //private ObservationManagedBean observationBean;

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
        // Get observation bean. ManagedProperty annotation doesn't seem to work (perhaps because
        // it's resolved later?) so we do it like this: http://stackoverflow.com/a/2633733
//        FacesContext context = FacesContext.getCurrentInstance();
//        observationBean = (ObservationManagedBean) context.getApplication()
//                .evaluateExpressionGet(context, "#{observationBean}", ObservationManagedBean.class);
//        
//        if (observationBean == null) {
//            this.observation = createTestObservation();
//        } else {
//            this.observation = observationBean.getObservation();
//        }
//        
//        this.observationDate = this.observation.observationDateStr();
//        this.observationDuration = this.observation.durationStr();
//        //this.max = new Date(this.observation.getEnd());
        
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
        for (DebugRecordEntity recording : observationBean.getRecords()) {
            String category = recording.getCategory();
            Date eventStart = new Date(recording.getStartTime());
            Date eventEnd = new Date(recording.getEndTime());
            TimelineEvent event = new TimelineEvent("", eventStart, eventEnd, true, category);
            if (!categories.contains(category)) {
                timeline.addGroup(new TimelineGroup(category, category));
                categories.add(category);
            }
            timeline.add(event);
        }
    }

    // Dummy data and sample classes for observation data
    // TODO: remove when observation can be obtained from backend beans
    private Observation createTestObservation() {
        Random random = new Random();
        Date now = new Date();
        
        Observation newObs = new Observation();
        newObs.setObservationDate(now);
        newObs.setEnd(0);

        String[] categories = new String[]{"Järjestelee", "Selittää tehtävää", "Ohjaa suoritusta", "Antaa palautetta", "Tarkkailee", "Oppilas suorittaa tehtävää"};
        int r = random.nextInt(categories.length);
        for (String category : categories) {
            int startGap = category.length();
            if (category.equals(categories[r])) {
                startGap = 0;
            }
            int count = random.nextInt(20 - 5) + 5;
            long end = startGap * 60 * 1000;
            for (int i = 0; i < count; i++) {
                long start = end + Math.round(Math.random()) * 30 * 1000;
                end = start + Math.round(4 + Math.random() * 100) * 1 * 1000;
                Recording recording = new Recording(category, start, end);
                newObs.add(recording);
                if (end > newObs.getEnd()) {
                    newObs.setEnd(end);
                }
            }
        }
        return newObs;
    }

    public static class Observation implements Iterable<Recording> {

        private final List<Recording> recordings;
        private Date observationDate;
        private long end;

        public Observation() {
            this.recordings = new ArrayList<>();
        }

        @Override
        public Iterator<Recording> iterator() {
            return recordings.iterator();
        }

        public void add(Recording recording) {
            this.recordings.add(recording);
        }

        public Date getObservationDate() {
            return observationDate;
        }

        public void setObservationDate(Date start) {
            this.observationDate = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        private String observationDateStr() {
            Locale locale = new Locale("fi", "FI"); // Originally used SummaryManagedBean's locale when this class wasn't static!
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
            return df.format(observationDate);
        }

        private String durationStr() {
            if (TimeUnit.MILLISECONDS.toHours(end) > 0) {
                return String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(end),
                        TimeUnit.MILLISECONDS.toMinutes(end) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(end) % TimeUnit.MINUTES.toSeconds(1));
            } else {
                return String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(end) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(end) % TimeUnit.MINUTES.toSeconds(1));
            }
        }
    }

    public static class Recording {

        private String category;
        private long start;
        private long end;

        public Recording(String category, long start, long end) {
            this.category = category;
            this.start = start;
            this.end = end;
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public void setEnd(long end) {
            this.end = end;
        }
    }
}
