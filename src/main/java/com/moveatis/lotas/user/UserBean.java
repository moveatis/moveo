package com.moveatis.lotas.user;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.User;
import javax.persistence.TypedQuery;

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

    @Override
    public UserEntity findByName(String firstName, String lastName) {
        TypedQuery<UserEntity> user = em.createNamedQuery("findUserByName", UserEntity.class);
        return user.setParameter("firstName", firstName).setParameter("lastName", lastName)
                .getSingleResult();
    }
    
}
