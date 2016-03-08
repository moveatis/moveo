package com.moveatis.lotas.managedbeans;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import com.moveatis.lotas.interfaces.Observation;

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
   
    /**
     * Creates a new instance of ObservationManagedBean
     */
    public ObservationManagedBean() {
        types = new ArrayList<>();
        types.add("Järjestelyä");
        types.add("Ohjeiden anto");
        types.add("Tehtävien suoritus");
        types.add("Palautteen anto");
        types.add("Lopettelu");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add("Dummy dataa");
        types.add(FacesContext.class.getPackage().getImplementationVersion());
    }
    
    public void CategorizedVariableActivated(String category) {
        observationEJB.categorizedObservationActivated(category);
    }
    
    public void CategorizedVariableDisabled(String category) {
        
    }
    
}
