package com.moveatis.lotas.managedbeans;

import com.moveatis.lotas.category.timezone.TimeZoneInformation;
import com.moveatis.lotas.facade.ObservationFacadeLocal;
import com.moveatis.lotas.observation.CategorizedObservationEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "observationBean")
@SessionScoped
public class ObservationManagedBean implements Serializable {
    
    @Inject
    private ObservationFacadeLocal observationEJB;
    
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
