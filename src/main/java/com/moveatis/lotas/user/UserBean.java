package com.moveatis.lotas.user;

import com.moveatis.lotas.interfaces.AbstractBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.User;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinalium at outlook.com>
 */
@Stateless
public class UserBean extends AbstractBean<UserEntity> implements User {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBean.class);

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
        try {
            TypedQuery<UserEntity> user = em.createNamedQuery("findUserByName", UserEntity.class);
            return user.setParameter("firstName", firstName).setParameter("lastName", lastName)
                    .getSingleResult();
        }catch(NoResultException nre) {
            LOGGER.debug("Käyttäjää ei löytynyt -> firstName: " + firstName + ", lastName: " + lastName);
            return null;
        }
    }
    
}
