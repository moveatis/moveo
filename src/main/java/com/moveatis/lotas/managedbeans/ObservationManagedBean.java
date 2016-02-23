package com.moveatis.lotas.managedbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "observationBean")
@SessionScoped
public class ObservationManagedBean implements Serializable {
    
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
    
}
