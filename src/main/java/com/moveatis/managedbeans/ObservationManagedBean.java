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
package com.moveatis.managedbeans;
import com.moveatis.event.EventEntity;
import com.moveatis.interfaces.Observation;
import com.moveatis.interfaces.Session;
import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.records.RecordEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "observationBean")
@SessionScoped
public class ObservationManagedBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationManagedBean.class);
    
    private static final long serialVersionUID = 1L;
    
    private ObservationEntity observationEntity;
    private List<ObservationCategorySet> categorySetsInUse;
    private EventEntity eventEntity;
    
    @EJB
    private Observation observationEJB;
    @Inject
    private Session sessionBean;
    
    private Long nextTag;

    public ObservationManagedBean() {
        
    }
    
    @PostConstruct
    public void init() {
        nextTag = 0L;
    }
    
    @PreDestroy
    public void destroy() {
        if(observationEntity != null) {
            if(!observationEntity.getUserWantsToSaveToDatabase()) {
                observationEJB.removeUnsavedObservation(observationEntity);
            }
        }
    }
    
    public void resetCategorySetsInUse() {
        this.categorySetsInUse = null;
    }
    
    public void setEventEntity(EventEntity eventEntity) {
        this.eventEntity = eventEntity;
    }
    
    public EventEntity getEventEntity() {
        return this.eventEntity;
    }
    
    public void startObservation() {
        this.observationEntity = new ObservationEntity();
        this.observationEntity.setEvent(eventEntity);
        // Summary view doesn't break if no records are added.
        // TODO: Should observer not let user continue, if there are no records?
        if(observationEntity.getRecords() == null) {
            observationEntity.setRecords(new ArrayList<RecordEntity>());
        }
    }

    public ObservationEntity getObservationEntity() {
        return observationEntity;
    }

    public void setObservationEntity(ObservationEntity observationEntity) {
        this.observationEntity = observationEntity;
    }

    public List<ObservationCategorySet> getCategorySetsInUse() {
        return categorySetsInUse;
    }

    public void setCategorySetsInUse(List<ObservationCategorySet> categorySetsInUse) {
        
        for(ObservationCategorySet observationCategorySet : categorySetsInUse) {
            for(ObservationCategory observationCategory : observationCategorySet.getCategories()) {
                if(observationCategory.getTag().equals(-1L)) {
                    observationCategory.setTag(getNextTag());
                }
            }
        }
        this.categorySetsInUse = categorySetsInUse;
    }

    public Long getNextTag() {
        LOGGER.debug("getNextTag -> " + nextTag);
        return nextTag++;
    }
    
    public void setObservationName(String name) {
        this.observationEntity.setName(name);
    }
    
    public void setObservationDuration(long duration) {
        this.observationEntity.setDuration(duration);
    }
    
    public void addRecord(RecordEntity record) {
        List<RecordEntity> records = observationEntity.getRecords();
        record.setObservation(observationEntity);
        record.setVoiceComment(null);
        
        if(records == null) {
            records = new ArrayList<>();
        }
        
        records.add(record);
        observationEntity.setRecords(records);
    }
    
    public void saveObservation() {
        observationEntity.setObserver(sessionBean.getLoggedInUser());
        /*
        * Client wanted that user has the ability to persist observation into 
        * database when he/she wants to. Since it was easier to build business logic
        * by creating the observationEntity when observation is done, the boolean flag
        * "userWantsToSaveToDatabase was added. This flag is true if user wants to save
        * the observation to database. If its false, we need remove this observation later.
        */
        observationEntity.setUserWantsToSaveToDatabase(false); 
        observationEJB.create(observationEntity);
    }
    
    public void saveObservationToDatabase() {
        observationEntity.setUserWantsToSaveToDatabase(true);
        observationEntity.setObservationCategorySets(new HashSet<>(getCategorySetsInUse()));
        observationEJB.edit(observationEntity);
    }
}
