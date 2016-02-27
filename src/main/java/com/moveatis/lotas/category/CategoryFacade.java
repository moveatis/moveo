package com.moveatis.lotas.category;

import com.moveatis.lotas.facade.CategoryFacadeLocal;
import com.moveatis.lotas.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class CategoryFacade extends AbstractFacade<CategoryEntity> implements CategoryFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryFacade() {
        super(CategoryEntity.class);
    }

    @Override
    public CategoryEntity find(String label) {
        TypedQuery<CategoryEntity> query = em.createNamedQuery("Category.findByLabel", CategoryEntity.class);
        query.setParameter("label", label);
        
        return query.getSingleResult();
    }
}
    
