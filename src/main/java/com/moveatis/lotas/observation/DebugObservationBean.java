package com.moveatis.lotas.observation;

import com.moveatis.lotas.timezone.TimeZoneInformation;
import com.moveatis.lotas.interfaces.AbstractBean;
import com.moveatis.lotas.interfaces.DebugObservation;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import com.moveatis.lotas.restful.DebugObservationEntity;
import com.moveatis.lotas.restful.DebugRecordEntity;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
public class DebugObservationBean extends AbstractBean<ObservationEntity> implements DebugObservation, Serializable {
    
    private static final long serialVersionUID = 1L;
    private GregorianCalendar calendar;
    
    private DebugObservationEntity observation;

    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException();
    }

    public DebugObservationBean() {
        super(ObservationEntity.class);
    }
    
    @PostConstruct
    public void initialize() {
        calendar = (GregorianCalendar) Calendar.getInstance(TimeZoneInformation.getTimeZone());
        observation = new DebugObservationEntity();
    }

    @Override
    public void categorizedObservationActivated(String categoryLabel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void categorizedObservationDeactivated(String category) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void addRecord(DebugRecordEntity recordEntity) {
        this.observation.addRecord(recordEntity);
    }
    
    @Override
    public List<DebugRecordEntity> getRecords() {
        return this.observation.getRecords();
    }

    @Override
    public Iterator<DebugRecordEntity> getIterator() {
        return this.observation.getIterator();
    }
}
