/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.roles;

import com.moveatis.abstracts.AbstractBean;
import com.moveatis.interfaces.Role;
import com.moveatis.user.IdentifiedUserEntity;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class RoleBean extends AbstractBean<AbstractRole> implements Role {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleBean.class);
    
    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;

    public RoleBean() {
        super(AbstractRole.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public void addSuperuserRoleToUser(IdentifiedUserEntity user) {
        SuperUserRoleEntity role = new SuperUserRoleEntity();
        addRoleToUser(role, user);
    }

    @Override
    public void addSuperuserRoleToUser(IdentifiedUserEntity user, Date startDate, Date endDate) {
        SuperUserRoleEntity role = new SuperUserRoleEntity();
        addRoleToUser(role, user, startDate, endDate);
    }

    @Override
    public void removeSuperuserRoleFromUser(IdentifiedUserEntity user) {
        TypedQuery typedQuery = em.createNamedQuery("findSuperUserRoleByUser", SuperUserRoleEntity.class);
        typedQuery.setParameter("userEntity", user);
        SuperUserRoleEntity role = (SuperUserRoleEntity)typedQuery.getSingleResult();
        if(role != null) {
            removeRoleFromUser(role);
        }
    }

    @Override
    public List<SuperUserRoleEntity> listSuperusers() {
        SuperUserRoleEntity role = new SuperUserRoleEntity();
        return (List<SuperUserRoleEntity>)listRoleUsers(role);
    }

    private void addRoleToUser(AbstractRole role, IdentifiedUserEntity user) {
        role.setUserEntity(user);
        super.create(role);
    }
    
    private void addRoleToUser(AbstractRole role, IdentifiedUserEntity user, Date startDate, Date endDate) {
        role.setUserEntity(user);
        role.setStartDate(startDate);
        role.setEndDate(endDate);
        super.create(role);
    }
    
    private void removeRoleFromUser(AbstractRole role) {
        super.remove(role);
    }
    
    public List<? extends AbstractRole> listRoleUsers(AbstractRole role) {
        return Collections.emptyList();
    }

    @Override
    public boolean checkIfUserIsSuperUser(IdentifiedUserEntity user) {
        
        TypedQuery typedQuery = em.createNamedQuery("findSuperUserRoleByUser", SuperUserRoleEntity.class);
        typedQuery.setParameter("userEntity", user);
        
        try {
            SuperUserRoleEntity role = (SuperUserRoleEntity)typedQuery.getSingleResult();
            if(role == null) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException nre) {
            return false;
        }
    }
}
