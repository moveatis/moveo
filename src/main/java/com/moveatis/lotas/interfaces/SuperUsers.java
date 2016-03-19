package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.superuser.SuperUsersEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(SuperUsers.class)
public interface SuperUsers {

    void create(SuperUsersEntity superUsers);

    void edit(SuperUsersEntity superUsers);

    void remove(SuperUsersEntity superUsers);

    SuperUsersEntity find(Object id);

    List<SuperUsersEntity> findAll();

    List<SuperUsersEntity> findRange(int[] range);

    int count();
    
}
