package com.moveatis.lotas.scene;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.SceneTemplate;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class SceneTemplateBean extends AbstractBean<SceneTemplateEntity> implements SceneTemplate {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SceneTemplateBean() {
        super(SceneTemplateEntity.class);
    }
    
}
