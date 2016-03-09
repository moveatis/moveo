package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.observation.ObservationEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(Observation.class)
public interface Observation {
    
    void categorizedObservationActivated(String category);
    void categorizedObservationDeactivated(String category);

    void create(ObservationEntity observationEntity);

    void edit(ObservationEntity observationEntity);

    void remove(ObservationEntity observationEntity);

    ObservationEntity find(Object id);

    List<ObservationEntity> findAll();

    List<ObservationEntity> findRange(int[] range);

    int count();
    
}
