/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moveatis.lotas.managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
@Named(value = "timelineChartBean")
public class TimelineChartManagedBean {

    private TimelineModel model;  
    private String locale = "en"; // current locale as String, java.util.Locale is possible too.
    private TimeZone timeZone = TimeZone.getTimeZone("Europe/Helsinki"); // Target time zone to convert start / end dates for displaying.
    private TimeZone browserTimeZone = TimeZone.getTimeZone("Europe/Helsinki"); // Time zone the user's browser / PC is running in.
    private Date min;
    private Date max;
    private long zoomMin;
    private long zoomMax;

    // Dummy observation object containing the observation data
    // TODO: get from backend
    private Observation observation;

    @PostConstruct
    protected void initialize() {
        createTestObservation(); // TODO: remove when observation got from backend
        createModel();
    }

    public TimelineModel getModel() {  
        return model;  
    }  

    public String getLocale() {  
        return locale;  
    }  

    public void setLocale(String locale) {  
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

    public long getZoomMin() {
        return zoomMin;
    }

    public long getZoomMax() {
        return zoomMax;
    }

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    private void createModel() {
        model = new TimelineModel();
        HashSet<String> categories = new HashSet<>();
        for (Recording recording : this.observation) {
            String category = recording.getCategory();
            TimelineEvent event = new TimelineEvent("", recording.getStart(),
                    recording.getEnd(), true, category);
            if (!categories.contains(category)) {
                model.addGroup(new TimelineGroup(category, category));
                categories.add(category);
            }
            model.add(event);
        }
    }

    // Dummy data and sample classes for observation data
    // TODO: remove when observation got from backend
    private void createTestObservation() {
        observation = new Observation();

        Random random = new Random();
        Date now = new Date();

        String[] Categories = new String[]{"Järjestelee", "Selittää tehtävää", "Ohjaa suoritusta", "Antaa palautetta", "Tarkkailee", "Oppilas suorittaa tehtävää"};

        for (String category : Categories) {
            Date end = new Date(now.getTime() + category.length() * 60 * 1000);
            int count = random.nextInt(20 - 5) + 5;
            for (int i = 0; i < count; i++) {
                Date start = new Date(end.getTime() + Math.round(Math.random()) * 30 * 1000);
                end = new Date(start.getTime() + Math.round(4 + Math.random() * 100) * 1 * 1000);
                Recording recording = new Recording(category, start, end);
                observation.add(recording);
            }
        }
    }

    class Observation implements Iterable<Recording> {

        private final List<Recording> recordings;

        public Observation() {
            this.recordings = new ArrayList<>();
        }

        private void add(Recording recording) {
            this.recordings.add(recording);
        }

        @Override
        public Iterator<Recording> iterator() {
            return recordings.iterator();
        }

    }

    class Recording {
        private Date start;
        private Date end;
        private String category;

        public Recording(String category, Date start, Date end) {
            this.category = category;
            this.start = start;
            this.end = end;
        }

        public Date getStart() {
            return start;
        }

        public Date getEnd() {
            return end;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public void setEnd(Date end) {
            this.end = end;
        }
    }
}
