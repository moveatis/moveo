package com.moveatis.lotas.observation;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.category.CategoryFacade;
import com.moveatis.lotas.category.timezone.TimeZoneInformation;
import com.moveatis.lotas.category.variable.CategorizedVariableEntity;
import com.moveatis.lotas.facade.ObservationFacadeLocal;
import com.moveatis.lotas.facade.AbstractFacade;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
public class ObservationFacade extends AbstractFacade<CategorizedObservationEntity> implements ObservationFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;
    
    @Inject
    private CategoryFacade categoryEJB;
    
    
    
    private GregorianCalendar calendar;
    private Date startTime, stopTime;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservationFacade() {
        super(CategorizedObservationEntity.class);
    }
    
    @PostConstruct
    public void initialize() {
        calendar = (GregorianCalendar) Calendar.getInstance(TimeZoneInformation.getTimeZone());
    }

    @Override
    public void categorizedObservationActivated(String categoryLabel) {
        CategorizedObservationEntity observationEntity = new CategorizedObservationEntity();
        CategorizedVariableEntity variableEntity = new CategorizedVariableEntity();
        
        observationEntity.setCreated((Date) calendar.getTime());
        observationEntity.setStartTime((Date) calendar.getTime());
        
        CategoryEntity category = categoryEJB.find(categoryLabel);
        if(category == null) {
            category = new CategoryEntity();
            category.setLabel(categoryLabel);
            category.setCreated((Date) calendar.getTime());
            category.setScene(null);
        }
    }

    @Override
    public void categorizedObservationDeactivated(String category) {
        
    }
    
}
