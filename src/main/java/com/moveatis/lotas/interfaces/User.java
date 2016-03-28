package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.user.UserEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(User.class)
public interface User {

    void create(UserEntity userEntity);

    void edit(UserEntity userEntity);

    void remove(UserEntity userEntity);

    UserEntity find(Object id);
    
    UserEntity findByName(String firstName, String lastName);

    List<UserEntity> findAll();

    List<UserEntity> findRange(int[] range);

    int count();
    
}
