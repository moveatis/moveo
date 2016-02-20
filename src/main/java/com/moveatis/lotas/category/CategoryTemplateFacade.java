package com.moveatis.lotas.category;

import com.moveatis.lotas.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class CategoryTemplateFacade extends AbstractFacade<CategoryTemplateEntity> implements CategoryTemplateFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryTemplateFacade() {
        super(CategoryTemplateEntity.class);
    }
    
}
