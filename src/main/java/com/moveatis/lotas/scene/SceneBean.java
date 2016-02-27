package com.moveatis.lotas.scene;

import com.moveatis.lotas.interfaces.AbstractBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.Scene;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class SceneBean extends AbstractBean<SceneEntity> implements Scene {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SceneBean() {
        super(SceneEntity.class);
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
    
}
