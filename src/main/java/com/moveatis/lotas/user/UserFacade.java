package com.moveatis.lotas.user;

import com.moveatis.lotas.facade.UserFacadeLocal;
import com.moveatis.lotas.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sami Kallio <phinalium at outlook.com>
 */
@Stateless
public class UserFacade extends AbstractFacade<UserEntity> implements UserFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(UserEntity.class);
    }
    
}
