package com.moveatis.lotas.scene;

import com.moveatis.lotas.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class SceneTemplateFacade extends AbstractFacade<SceneTemplateEntity> implements SceneTemplateFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SceneTemplateFacade() {
        super(SceneTemplateEntity.class);
    }
    
}
