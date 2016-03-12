package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.observation.ObservationEntity;
import com.moveatis.lotas.restful.DebugRecordEntity;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(DebugObservation.class)
public interface DebugObservation {
    
    void categorizedObservationActivated(String category);
    void categorizedObservationDeactivated(String category);

    void create(ObservationEntity observationEntity);

    void edit(ObservationEntity observationEntity);

    void remove(ObservationEntity observationEntity);

    ObservationEntity find(Object id);

    List<ObservationEntity> findAll();

    List<ObservationEntity> findRange(int[] range);

    int count();
    
    void addRecord(DebugRecordEntity recordEntity);
    
    List<DebugRecordEntity> getRecords();
    
    Iterator<DebugRecordEntity> getIterator();
    
}
