package com.moveatis.lotas.managedbeans;
import com.moveatis.lotas.interfaces.Observation;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "observationBean")
@SessionScoped
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

    /**
     * Creates a new instance of ObservationManagedBean
     */
    public ObservationManagedBean() {
        
    }
}
