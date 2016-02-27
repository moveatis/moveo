package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.observation.CategorizedObservationEntity;
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

    void create(CategorizedObservationEntity observationEntity);

    void edit(CategorizedObservationEntity observationEntity);

    void remove(CategorizedObservationEntity observationEntity);

    CategorizedObservationEntity find(Object id);

    List<CategorizedObservationEntity> findAll();

    List<CategorizedObservationEntity> findRange(int[] range);

    int count();
    
}
