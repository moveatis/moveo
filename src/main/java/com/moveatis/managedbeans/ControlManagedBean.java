/*
 * Copyright (c) 2016, sami 
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
import com.moveatis.event.EventGroupEntity;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.Session;
import com.moveatis.user.AbstractUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value="controlManagedBean")
@ViewScoped
public class ControlManagedBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlManagedBean.class);

    private List<EventGroupEntity> ownEventGroups;
    private List<EventGroupEntity> accessEventGroups;
    private List<EventGroupEntity> publicEventGroups;
    
    private List<String> ownEvents;
    private List<String> accessEvents;
    private List<String> publicEvents;
    
    private MenuModel menuModel;
    
    @Inject
    private EventGroup eventGroupEJB;
    
    @Inject
    private Session sessionBean;
    
    private AbstractUser user;
    
    public ControlManagedBean() {
        
    }
    
    @PostConstruct
    public void init() {
        menuModel = new DefaultMenuModel();
        user = sessionBean.getLoggedInUser();
        
        setOwnEventGroups();
        setAccessEventGroups();
        
        DefaultSubMenu ownEventGroupsMenu = new DefaultSubMenu("Omat");
        
        if(!ownEventGroups.isEmpty()) {
            for (EventGroupEntity groupEntity : ownEventGroups) {
                DefaultSubMenu groupItem = new DefaultSubMenu(groupEntity.getLabel());
                
                for(EventEntity eventEntity : groupEntity.getEvents()) {
                    DefaultSubMenu eventItem = new DefaultSubMenu(eventEntity.getLabel());
                    
                    DefaultMenuItem menuItem = new DefaultMenuItem("Uusi observointi");
                    menuItem.setCommand("#{controlManagedBean.newObservation('m1')}");
                    
                    eventItem.addElement(menuItem);
                    groupItem.addElement(eventItem);
                }
                ownEventGroupsMenu.addElement(groupItem);
            }
        }
        
        DefaultMenuItem newOwnEvent = new DefaultMenuItem("Uusi tapahtuma");
        ownEventGroupsMenu.addElement(newOwnEvent);
        
        menuModel.addElement(ownEventGroupsMenu);
        
        DefaultSubMenu accessEventGroupsMenu = new DefaultSubMenu("Käyttöoikeus");
        
        if(!accessEventGroups.isEmpty()) {
            for(EventGroupEntity groupEntity : accessEventGroups) {
                DefaultSubMenu groupItem = new DefaultSubMenu(groupEntity.getLabel());
                
                for(EventEntity eventEntity : groupEntity.getEvents()) {
                    DefaultSubMenu eventItem = new DefaultSubMenu(eventEntity.getLabel());
                    
                    DefaultMenuItem menuItem = new DefaultMenuItem("Uusi observointi");
                    menuItem.setCommand("#{controlManagedBean.newObservation('m1')}");
                    
                    eventItem.addElement(menuItem);
                    groupItem.addElement(eventItem);
                }
                
                accessEventGroupsMenu.addElement(groupItem);
            }
        }
        
        DefaultMenuItem newAccessEvent = new DefaultMenuItem("Uusi tapahtuma");
        accessEventGroupsMenu.addElement(newAccessEvent);
        
        menuModel.addElement(accessEventGroupsMenu);
        
        DefaultSubMenu publicEventGroupsMenu = new DefaultSubMenu("Yleiset");
        
        menuModel.addElement(publicEventGroupsMenu);
    }
    
    private void setOwnEventGroups() {
        ownEventGroups = eventGroupEJB.findAllForOwner(user);
    }

    private void setAccessEventGroups() {
        accessEventGroups = eventGroupEJB.findAllForUser(user);
    }

    private void setPublicEventGroups() {
        
    }
    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }
    
    public String newObservation(String identifier) {
        return "newobservation";
    }
    
}
