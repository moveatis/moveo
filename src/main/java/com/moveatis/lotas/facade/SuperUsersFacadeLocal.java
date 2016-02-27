/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moveatis.lotas.facade;

import com.moveatis.lotas.category.application.SuperUsersEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sami
 */
@Local
public interface SuperUsersFacadeLocal {

    void create(SuperUsersEntity superUsers);

    void edit(SuperUsersEntity superUsers);

    void remove(SuperUsersEntity superUsers);

    SuperUsersEntity find(Object id);

    List<SuperUsersEntity> findAll();

    List<SuperUsersEntity> findRange(int[] range);

    int count();
    
}
