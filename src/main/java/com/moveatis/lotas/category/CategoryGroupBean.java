package com.moveatis.lotas.category;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.CategoryGroup;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class CategoryGroupBean extends AbstractBean<CategoryGroupEntity> implements CategoryGroup {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryGroupBean() {
        super(CategoryGroupEntity.class);
    }
}
