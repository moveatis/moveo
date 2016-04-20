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
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Session;
import com.moveatis.user.AbstractUser;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@Named(value = "controlManagedBean")
@ViewScoped
public class ControlManagedBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControlManagedBean.class);

    private List<EventGroupEntity> ownEventGroups;
    private List<EventGroupEntity> sharedEventGroups;
    private List<EventGroupEntity> publicEventGroups;

    private List<String> ownEvents;
    private List<String> sharedEvents;
    private List<String> publicEvents;

    private String eventGroupName = "";
    private String eventGroupDescription = "";
    private String eventName = "";
    private String eventDescription = "";

    private MenuModel menuModel;

    @Inject
    private EventGroup eventGroupEJB;

    @Inject
    private Session sessionBean;

    @Inject
    @MessageBundle
    private transient ResourceBundle messages;

    private AbstractUser user;

    public ControlManagedBean() {
    }

    @PostConstruct
    public void init() {
        user = sessionBean.getLoggedInUser();
        setOwnEventGroups();
        setAccessEventGroups();
        createMenuModel();
    }

    private void setOwnEventGroups() {
        ownEventGroups = eventGroupEJB.findAllForOwner(user);
    }

    private void setAccessEventGroups() {
        sharedEventGroups = eventGroupEJB.findAllForUser(user);
    }

    private void setPublicEventGroups() {
        //TODO: get all public event groups
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

    private void createMenuModel() {
        menuModel = new DefaultMenuModel();
        menuModel.addElement(createSubMenuModel("Omat", ownEventGroups));
        menuModel.addElement(createSubMenuModel("Jaetut", sharedEventGroups));
        menuModel.addElement(createSubMenuModel("Julkiset", publicEventGroups));
    }

    private DefaultSubMenu createSubMenuModel(String menuName, List<EventGroupEntity> eventGroups) {
        DefaultSubMenu menuEventGroups = new DefaultSubMenu(menuName);

        if (eventGroups != null && !eventGroups.isEmpty()) {

            for (EventGroupEntity eventGroup : eventGroups) {

                DefaultSubMenu subMenuEventGroup = new DefaultSubMenu(eventGroup.getLabel());

                for (EventEntity event : eventGroup.getEvents()) {
                    DefaultSubMenu subMenuEvent = new DefaultSubMenu(event.getLabel());
                    subMenuEvent.addElement(
                            createNewAddMenuItem(messages.getString("con_newObservation"),
                                    null, "#{controlManagedBean.newObservation('" + event.getId() + "')}"));
                    subMenuEventGroup.addElement(subMenuEvent);
                }
                subMenuEventGroup.addElement(
                        createNewAddMenuItem(messages.getString("con_newEvent"),
                                "PF('dlgEvent').show();", null));
                menuEventGroups.addElement(subMenuEventGroup);
            }
        }
        menuEventGroups.addElement(
                createNewAddMenuItem(messages.getString("con_newEventGroup"),
                        "PF('dlgEventGroup').show();", null));
        return menuEventGroups;
    }

    private DefaultMenuItem createNewAddMenuItem(String value, String onClick, String command) {
        DefaultMenuItem menuItem = new DefaultMenuItem(value, "ui-icon-plusthick");
        if (onClick != null) {
            menuItem.setOnclick(onClick);
        }
        if (command != null) {
            menuItem.setCommand(command);
        }
        return menuItem;
    }

    public void createNewEventGroup() {
        EventGroupEntity eventGroup = new EventGroupEntity();
        eventGroup.setLabel(eventGroupName);
        eventGroup.setOwner(user);
        eventGroupEJB.create(eventGroup);
        init();
    }

    public String getEventGroupName() {
        return eventGroupName;
    }

    public void setEventGroupName(String eventGroupName) {
        this.eventGroupName = eventGroupName;
    }

    public String getEventGroupDescription() {
        return eventGroupDescription;
    }

    public void setEventGroupDescription(String eventGroupDescription) {
        this.eventGroupDescription = eventGroupDescription;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
