package com.moveatis.lotas.user;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface UserFacadeLocal {

    void create(UserEntity userEntity);

    void edit(UserEntity userEntity);

    void remove(UserEntity userEntity);

    UserEntity find(Object id);

    List<UserEntity> findAll();

    List<UserEntity> findRange(int[] range);

    int count();
    
}
