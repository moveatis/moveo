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
package com.moveatis.observation;

import com.moveatis.abstracts.AbstractBean;
import com.moveatis.event.EventEntity;
import com.moveatis.interfaces.Event;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.interfaces.Observation;
import com.moveatis.records.RecordEntity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.moveatis.repository.RepositoryDescriptorBean;
import com.moveatis.session.SessionBean;
import com.moveatis.user.AbstractUser;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
public class ObservationBean extends AbstractBean<ObservationEntity> implements Observation, Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationBean.class);
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private SessionBean sessionBean;
    
    @EJB
    private RepositoryDescriptorBean repositoryBean;
    @EJB
    private Event eventEJB;
    
    private ObservationEntity observationEntity;

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;
        
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservationBean() {
        super(ObservationEntity.class);
    }

    @Override
    public List<ObservationEntity> findAllByObserver(AbstractUser observer) {
        TypedQuery<ObservationEntity> query = em.createNamedQuery("findByObserver", ObservationEntity.class);
        query.setParameter("observer", observer);
        return query.getResultList();
    }

    @Override
    public List<ObservationEntity> findWithoutEvent(AbstractUser observer) {
        TypedQuery<ObservationEntity> query = em.createNamedQuery("findWithoutEvent", ObservationEntity.class);
        query.setParameter("observer", observer);
        return query.getResultList();
    }

    @Override
    public List<ObservationEntity> findByEventsNotOwned(AbstractUser observer) {
        TypedQuery<ObservationEntity> query = em.createNamedQuery("findByEventsNotOwned", ObservationEntity.class);
        query.setParameter("observer", observer);
        return query.getResultList();
    }

    @Override
    public void create(ObservationEntity observationEntity) {
        if (sessionBean.isIdentifiedUser()) {
            super.create(observationEntity);
        } else {
            /*
            * TODO: Create observationentity to repository, not database
            */
            super.create(observationEntity);
        }
    }

    @Override
    public List<RecordEntity> findRecords(Object id) {
        observationEntity = em.find(ObservationEntity.class, id);
        LOGGER.debug("observation-entity records size ->" + observationEntity.getRecords().size());
        return observationEntity.getRecords();
    }
    
    @Override
    public void remove(ObservationEntity observationEntity) {
        super.remove(observationEntity);
        eventEJB.removeObservation(observationEntity);
        observationEntity.setEvent(null);
        super.edit(observationEntity);
    }

    @Override
    public void removeUnsavedObservation(ObservationEntity observationEntity) {
        em.remove(em.merge(observationEntity));
    }
}
