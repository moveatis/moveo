package com.moveatis.lotas.observation;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "observationBean")
@SessionScoped
public class ObservationManagedBean implements Serializable {

    /**
     * Creates a new instance of ObservationManagedBean
     */
    public ObservationManagedBean() {
    }
    
}
