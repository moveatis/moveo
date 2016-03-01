package com.moveatis.lotas.observation;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.interfaces.Category;
import com.moveatis.lotas.timezone.TimeZoneInformation;
import com.moveatis.lotas.interfaces.AbstractBean;
import com.moveatis.lotas.scene.SceneBean;
import com.moveatis.lotas.user.UserBean;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.interfaces.Scene;
import com.moveatis.lotas.interfaces.User;
import java.io.Serializable;
import javax.ejb.EJB;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
public class ObservationBean extends AbstractBean<ObservationEntity> implements Observation, Serializable {
    
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;
    
    @EJB
    private Category categoryEJB;
    
    @EJB
    private Scene sceneEJB;
    
    @EJB
    private User userEJB;
    
    private GregorianCalendar calendar;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservationBean() {
        super(ObservationEntity.class);
    }
    
    @PostConstruct
    public void initialize() {
        calendar = (GregorianCalendar) Calendar.getInstance(TimeZoneInformation.getTimeZone());
    }

    @Override
    public void categorizedObservationActivated(String categoryLabel) {
        ObservationEntity observationEntity = new ObservationEntity();
        
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
