/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moveatis.lotas.category.application;

import com.moveatis.lotas.facade.AbstractFacade;
import com.moveatis.lotas.facade.SuperUsersFacadeLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author sami
 */
@Stateless
public class SuperUsersFacade extends AbstractFacade<SuperUsersEntity> implements SuperUsersFacadeLocal {

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SuperUsersFacade() {
        super(SuperUsersEntity.class);
    }
    
}
