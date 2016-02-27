package com.moveatis.lotas.category;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.CategoryTemplate;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class CategoryTemplateBean extends AbstractBean<CategoryTemplateEntity> implements CategoryTemplate {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryTemplateBean() {
        super(CategoryTemplateEntity.class);
    }
}
