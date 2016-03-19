package com.moveatis.lotas.event;

import com.moveatis.lotas.interfaces.AbstractBean;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.Scene;
import com.moveatis.lotas.user.UserEntity;
import javax.ejb.Stateful;
import javax.persistence.TypedQuery;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
public class EventBean extends AbstractBean<EventEntity> implements Scene {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;
    
    private EventEntity sceneEntity;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventBean() {
        super(EventEntity.class);
    }

    @Override
    public List<String> getCategories() {
        //this is just dummy data for now
        ArrayList<String> categories = new ArrayList<>();
        
        for(int i = 0; i < 10; i++) {
            categories.add("Testi " + (i + 1));

        }
        
        return categories;
    }

    @Override
    public EventEntity getSceneEntity() {
        if(this.sceneEntity == null) {
            
        }
        
        return this.sceneEntity;
    }

    @Override
    public List<EventEntity> findScenesForUser(UserEntity user) {
        TypedQuery<EventEntity> query = em.createNamedQuery("SceneEntity.findByUser", EventEntity.class);
        query.setParameter("owner", user);
        return query.getResultList();
    }
    
}
