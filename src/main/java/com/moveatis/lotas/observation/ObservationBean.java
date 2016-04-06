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
package com.moveatis.lotas.observation;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.devel.ObservationFileOperations;
import com.moveatis.lotas.interfaces.Category;
import com.moveatis.lotas.timezone.TimeZoneInformation;
import com.moveatis.lotas.interfaces.AbstractBean;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.moveatis.lotas.interfaces.Observation;
import com.moveatis.lotas.interfaces.Scene;
import com.moveatis.lotas.interfaces.Session;
import com.moveatis.lotas.interfaces.User;
import com.moveatis.lotas.records.RecordEntity;
import com.moveatis.lotas.user.UserEntity;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateful
public class ObservationBean extends AbstractBean<ObservationEntity> implements Observation, Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationBean.class);
    
    private static final long serialVersionUID = 1L;
    
    /*
    * For development phase we use local file, not database
    */
    private ObservationFileOperations develFileOperations;

    @PersistenceContext(unitName = "LOTAS_PERSISTENCE")
    private EntityManager em;
    
    @EJB
    private Category categoryEJB;
    
    @EJB
    private Scene sceneEJB;
    
    @EJB
    private User userEJB;
    
    @Inject
    private Session sessionBean;
    
    private GregorianCalendar calendar;
    
    private ObservationEntity observation;
    private UserEntity user;
    
    
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservationBean() {
        super(ObservationEntity.class);
    }
    
    @PostConstruct
    public void initialize() {
        calendar = (GregorianCalendar) Calendar.getInstance(TimeZoneInformation.getTimeZone());
        develFileOperations = new ObservationFileOperations();
        
        //For devel-phase there is just one user
        user = userEJB.find(1L);
        if(user == null) {
            user = new UserEntity();
            userEJB.create(user);
        }
        
        LOGGER.debug("Sessionbean returns following information ->" + sessionBean.toString());
    }

    @Override
    public void categorizedObservationActivated(String categoryLabel) {
        ObservationEntity observationEntity = new ObservationEntity();
        
        observationEntity.setCreated((Date) calendar.getTime());
        
        CategoryEntity category = categoryEJB.find(categoryLabel);
        if(category == null) {
            category = new CategoryEntity();
            category.setLabel(categoryLabel);
            category.setCreated((Date) calendar.getTime());
        }
    }

    @Override
    public void categorizedObservationDeactivated(String category) {
        
    }
    
    @Override
    public void setDate(java.util.Date date) {
        develFileOperations.writeDate(date);
    }
    
    @Override
    public java.util.Date getDate() {
        return develFileOperations.readDate();
    }
    
    @Override
    public void setDuration(long duration) {
        develFileOperations.writeDuration(duration);
    }
    
    @Override
    public long getDuration() {
        return develFileOperations.readDuration();
    }

    @Override
    public void addRecord(RecordEntity recordEntity) {
        
        develFileOperations.write(recordEntity);
        
        LOGGER.debug("Category -> " + recordEntity.getCategory());
        LOGGER.debug("Start time -> " + recordEntity.getStartTime());
        LOGGER.debug("End time -> " + recordEntity.getEndTime());
        
        LOGGER.debug("addRecord lopetettu");
    }

    @Override
    public List<RecordEntity> getRecords() {
        return develFileOperations.read();
    }
}
