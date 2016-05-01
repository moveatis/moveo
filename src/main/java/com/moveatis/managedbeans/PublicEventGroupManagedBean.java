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

import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.interfaces.Event;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.Session;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value="publicEventGroupManagedBean")
@ViewScoped
public class PublicEventGroupManagedBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicEventGroupManagedBean.class);
    
    private static final long serialVersionUID = 1L;
    
    private Long selectedPublicEventGroupId;
    private EventGroupEntity selectedEventGroupEntity;
    private Map<String, Long> publicEventGroupsMap;
    
    private Long selectedPublicEventId;
    private EventEntity selectedEventEntity;
    private Map<String, Long> publicEventsMap;
    
    @Inject
    private Session sessionBean;
    @Inject
    private EventGroup eventGroupEJB;
    @Inject
    private Event eventEJB;
    
    @Inject
    private PublicCategorySelectionManagedBean publicCategorySelectionManagedBean;

    /** Creates a new instance of PublicEventGroupManagerBean */
    public PublicEventGroupManagedBean() {
        
    }

    public Long getSelectedPublicEventGroupId() {
        return selectedPublicEventGroupId;
    }

    public void setSelectedPublicEventGroupId(Long selectedPublicEventGroupId) {
        this.selectedPublicEventGroupId = selectedPublicEventGroupId;
    }

    public Map<String, Long> getPublicEventGroupsMap() {
        if(publicEventGroupsMap == null) {
            publicEventGroupsMap = new TreeMap<>();
            
            List<EventGroupEntity> publicEventGroupList = eventGroupEJB.findAllForPublicUser();
            for(EventGroupEntity eventGroupEntity : publicEventGroupList) {
                publicEventGroupsMap.put(eventGroupEntity.getLabel(), eventGroupEntity.getId());
            }
        }
        return publicEventGroupsMap;
    }

    public void setPublicEventGroupsMap(Map<String, Long> publicEventGroupsMap) {
        this.publicEventGroupsMap = publicEventGroupsMap;
    }

    public Long getSelectedPublicEventId() {
        return selectedPublicEventId;
    }

    public void setSelectedPublicEventId(Long selectedPublicEventId) {
        this.selectedPublicEventId = selectedPublicEventId;
    }

    public Map<String, Long> getPublicEventsMap() {
        return publicEventsMap;
    }

    public void setPublicEventsMap(Map<String, Long> publicEventsMap) {
        this.publicEventsMap = publicEventsMap;
    }
    
    public void setPublicEventGroup(ActionEvent actionEvent) {
        selectedEventGroupEntity = eventGroupEJB.find(selectedPublicEventGroupId);
        publicEventsMap = new TreeMap<>();
        
        for(EventEntity eventEntity: selectedEventGroupEntity.getEvents()) {
            publicEventsMap.put(eventEntity.getLabel(), eventEntity.getId());
        }
    }
    
    public void setPublicEvent(ActionEvent actionEvent) {
        selectedEventEntity = eventEJB.find(selectedPublicEventId);
        LOGGER.debug("selectedEventEntity set -> " + selectedEventEntity.getId());
        sessionBean.setEventEntityForNewObservation(selectedEventEntity);
        Set<CategorySetEntity> categorySets = selectedEventGroupEntity.getCategorySets();
        publicCategorySelectionManagedBean.setPublicCategorySetsInSelection(categorySets);
    }
}
