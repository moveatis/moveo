package com.moveatis.lotas.user;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.User;

/**
 *
 * @author Sami Kallio <phinalium at outlook.com>
 */
@Stateless
public class UserBean extends AbstractBean<UserEntity> implements User {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserBean() {
        super(UserEntity.class);
    }
    
}
