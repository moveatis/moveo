package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "observationBean")
@SessionScoped
public class ObservationManagedBean implements Serializable {
    
    class Recording {
        private String category;
        private long start;
        private long end;
        
        public Recording(String category, long start, long end) {
            this.category = category;
            this.start = start;
            this.end = end;
        }
        
        private String timeToString(long ms) {
            long t = ms / 1000;
            long m = t / 60;
            long s = t - m * 60;
            return String.format("%02d:%02d", m, s);
        }
        
        @Override
        public String toString() {
            return category + " " + timeToString(start) + " --> " + timeToString(end);
        }
    }
    
    class Observation {
        private List<Recording> recordings;
        
        public Observation() {
            recordings = new ArrayList<>();
        }
        
        public void add(Recording recording) {
            recordings.add(recording);
        }
        
        public Recording getLastRecording() {
            int recordingCount = recordings.size();
            if (recordingCount > 0) {
                return recordings.get(recordingCount - 1);
            }
            return null;
        }
    }
    
    //
    //
    //
    
    private Observation observation;
    private String recording = "no recording";

    /**
     * Creates a new instance of ObservationManagedBean
     */
    public ObservationManagedBean() {
        observation = new Observation();
    }
    
    public void addRecording() {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        String category = requestParameters.get("rec-category");
        String startTime = requestParameters.get("rec-start-time");
        String endTime = requestParameters.get("rec-end-time");
        
        long start;
        long end;
        
        try {
            start = Long.parseLong(startTime);
            end = Long.parseLong(endTime);
        } catch (NumberFormatException e) {
            return;
        }
        
        Recording r = new Recording(category, start, end);
        observation.add(r);
        recording = r.toString();
    }
    
    public String getRecording() {
        return recording;
    }
}
