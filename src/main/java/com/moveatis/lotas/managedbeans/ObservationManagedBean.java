package com.moveatis.lotas.managedbeans;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import com.moveatis.lotas.interfaces.Observation;
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

    /**
     * Creates a new instance of ObservationManagedBean
     */
    public ObservationManagedBean() {
        
    }
}
