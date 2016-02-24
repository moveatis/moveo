package com.moveatis.lotas.scene;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.facade.SceneFacadeLocal;
import com.moveatis.lotas.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class SceneFacade extends AbstractFacade<SceneEntity> implements SceneFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SceneFacade() {
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
