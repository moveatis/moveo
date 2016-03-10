package com.moveatis.lotas.managedbeans;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import com.moveatis.lotas.interfaces.Observation;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@ManagedBean
@SessionScoped
@Named(value = "observationBean")
public class ObservationManagedBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @EJB
    private Observation observationEJB;
    
    private List<String> types;

    public List<String> getTypes() {
        return types;
    }
    
    public void CategorizedVariableActivated(String category) {
        observationEJB.categorizedObservationActivated(category);
    }
    
    public void CategorizedVariableDisabled(String category) {
        
    }
    
    //
    //
    //
    
//    public static class Category {
//        private String name;
//        private long time;
//        
//        public Category(String name) {
//            this.name = name;
//            this.time = 0;
//        }
//        
//        public String getName() {
//            return name;
//        }
//        
//        public long getTime() {
//            return time;
//        }
//    }
    
    private SummaryManagedBean.Observation observation;
    private String recordingStr = "no recording";
//    private long initialTime;
//    private List<Category> categoryData;

    /**
     * Creates a new instance of ObservationManagedBean
     */
    public ObservationManagedBean() {
        Date now = new Date();
        
        observation = new SummaryManagedBean.Observation();
        observation.setObservationDate(now);
        observation.setEnd(0);
        
//        initialTime = 0;
//        categoryData = new ArrayList<>();
//        categoryData.add(new Category("Järjestelyt"));
//        categoryData.add(new Category("Tehtävän selitys"));
//        categoryData.add(new Category("Ohjaus"));
//        categoryData.add(new Category("Palautteen anto"));
//        categoryData.add(new Category("Tarkkailu"));
//        categoryData.add(new Category("Muu toiminta"));
//        categoryData.add(new Category("Oppilas suorittaa tehtävää"));
    }
    
    public SummaryManagedBean.Observation getObservation() {
        return observation;
    }
    
    public String getRecordingStr() {
        return recordingStr;
    }
    
//    public long getInitialTime() {
//        return initialTime;
//    }
//    
//    public List<Category> getCategoryData() {
//        return categoryData;
//    }
    
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
        
        SummaryManagedBean.Recording recording = new SummaryManagedBean.Recording(category, start, end);
        observation.add(recording);
        if (end > observation.getEnd()) {
            observation.setEnd(end);
        }
        
        recordingStr = category + ": " + start + " --> " + end;
    }
}
