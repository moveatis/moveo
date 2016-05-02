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

import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.interfaces.Category;
import com.moveatis.interfaces.CategorySet;
import com.moveatis.interfaces.Event;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.MessageBundle;
import com.moveatis.interfaces.Session;
import com.moveatis.user.AbstractUser;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
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

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ControlManagedBean.class);

    private List<EventGroupEntity> eventGroups;

    private EventGroupEntity selectedEventGroup;
    private EventEntity selectedEvent;

    private CategorySetEntity selectedCategorySet;
    private CategoryEntity selectedCategory;

    private MenuModel controlMenuModel;
    private MenuModel observationsMenuModel;

    @Inject
    private EventGroup eventGroupEJB;
    @Inject
    private CategorySet categorySetEJB;
    @Inject
    private Category categoryEJB;

    @Inject
    private Event eventEJB;

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
        eventGroups = eventGroupEJB.findAllForOwner(user);
        createControlMenuModel();
        createObservationsMenuModel();
    }

    public MenuModel getControlMenuModel() {
        return controlMenuModel;
    }

    public void setControlMenuModel(MenuModel controlMenuModel) {
        this.controlMenuModel = controlMenuModel;
    }

    public MenuModel getObservationsMenuModel() {
        return observationsMenuModel;
    }

    public void setObservationsMenuModel(MenuModel observationsMenuModel) {
        this.observationsMenuModel = observationsMenuModel;
    }

    public void addEventGroup(EventGroupEntity eventGroup) {
        init();
    }

    public void addCategorySet(CategorySetEntity categorySet) {
        init();
    }

    public void addCategory(CategoryEntity categorEntity) {
        init();
    }

    private void createObservationsMenuModel() {
        observationsMenuModel = new DefaultMenuModel();
    }

    private void createControlMenuModel() {
        controlMenuModel = new DefaultMenuModel();
        controlMenuModel.addElement(createControlSubMenuModel("Tapahtumaryhmät", eventGroups));
    }

    private DefaultSubMenu createControlSubMenuModel(String menuName, List<EventGroupEntity> eventGroups) {

        DefaultSubMenu menuEventGroups = new DefaultSubMenu(menuName);
        menuEventGroups.addElement(
                createMenuItem(messages.getString("con_newEventGroup"), "fa fa-plus",
                        "PF('dlgEventGroup').show();", null, null, null, null, null));

        if (eventGroups != null && !eventGroups.isEmpty()) {

            for (EventGroupEntity eventGroup : eventGroups) {

                // Skip removed event groups
                if (eventGroup.getRemoved() != null) {
                    continue;
                }

                DefaultSubMenu subMenuEventGroup = new DefaultSubMenu(eventGroup.getLabel());

                for (CategorySetEntity categorySet : eventGroup.getCategorySets()) {

                    DefaultSubMenu subMenuCategorySet = new DefaultSubMenu(categorySet.getLabel());

                    subMenuCategorySet.addElement(
                            createMenuItem(messages.getString("con_newCategory"), "fa fa-plus",
                                    "PF('dlgCategory').show();",
                                    "#{controlManagedBean.setSelectedCategorySet}",
                                    "categorySetId", Long.toString(categorySet.getId()),
                                    null, null
                            ));

                    subMenuEventGroup.addElement(
                            createMenuItem(messages.getString("dlg_edit"), "fa fa-edit",
                                    "PF('dlgEditCategorySet').show();",
                                    "#{controlManagedBean.setSelectedCategorySet}",
                                    "categorySetId", Long.toString(categorySet.getId()),
                                    ":form-editCategorySet", "edit-menuItem"
                            ));

                    subMenuEventGroup.addElement(subMenuCategorySet);

                    for (CategoryEntity categoryEntity : categorySet.getCategoryEntitys().values()) {

                        DefaultMenuItem subMenuCategory = new DefaultMenuItem(categoryEntity.getLabel().getLabel());

                        subMenuCategorySet.addElement(
                                createMenuItem(messages.getString("dlg_edit"), "fa fa-edit",
                                        "PF('dlgEditCategory').show();",
                                        "#{controlManagedBean.setSelectedCategory}",
                                        "categoryId", Long.toString(categoryEntity.getId()),
                                        ":form-editCategory", "edit-menuItem"
                                ));

                        subMenuCategorySet.addElement(subMenuCategory);
                    }
                }
                for (EventEntity event : eventGroup.getEvents()) {

                    // Skip removed events
                    if (event.getRemoved() != null) {
                        continue;
                    }

                    DefaultSubMenu subMenuEvent = new DefaultSubMenu(event.getLabel());
                    subMenuEvent.addElement(
                            createMenuItem(messages.getString("con_newObservation"), "fa fa-play",
                                    null, "#{controlManagedBean.newObservation}",
                                    "eventId", Long.toString(event.getId()),
                                    null, null));
                    subMenuEventGroup.addElement(
                            createMenuItem(messages.getString("dlg_edit"), "fa fa-edit",
                                    "PF('dlgEditEvent').show();",
                                    "#{controlManagedBean.setSelectedEvent}",
                                    "eventId", Long.toString(event.getId()),
                                    ":form-editEvent", "edit-menuItem"));
                    subMenuEventGroup.addElement(subMenuEvent);
                }
                subMenuEventGroup.addElement(
                        createMenuItem(messages.getString("con_newCategorySet"), "fa fa-plus",
                                "PF('dlgCategorySet').show();",
                                "#{controlManagedBean.setSelectedEventGroup}",
                                "eventGroupId", Long.toString(eventGroup.getId()),
                                null, null));
                subMenuEventGroup.addElement(
                        createMenuItem(messages.getString("con_newEvent"), "fa fa-plus",
                                "PF('dlgEvent').show();",
                                "#{controlManagedBean.setSelectedEventGroup}",
                                "eventGroupId", Long.toString(eventGroup.getId()),
                                null, null));
                menuEventGroups.addElement(
                        createMenuItem(messages.getString("dlg_edit"), "fa fa-edit",
                                "PF('dlgEditEventGroup').show();",
                                "#{controlManagedBean.setSelectedEventGroup}",
                                "eventGroupId", Long.toString(eventGroup.getId()),
                                ":form-editEventGroup", "edit-menuItem"));
                menuEventGroups.addElement(subMenuEventGroup);
            }
        }
        return menuEventGroups;
    }

    private DefaultMenuItem createMenuItem(String value, String icon, String onClick, String command, String parameterName, String parameter,
            String update, String styleClass) {
        DefaultMenuItem menuItem = new DefaultMenuItem(value, icon);
        if (onClick != null) {
            menuItem.setOnclick(onClick);
        }
        if (command != null) {
            menuItem.setCommand(command);
        }
        if (parameterName != null && parameter != null) {
            menuItem.setParam(parameterName, parameter);
        }
        if (update != null) {
            menuItem.setUpdate(update);
        }
        if (styleClass != null) {
            menuItem.setStyleClass(styleClass);
        }

        return menuItem;
    }

    public void createNewEvent() {
        init();
    }

    public void editEventGroup() {
        if (selectedEventGroup == null) {
            return;
        }
        eventGroupEJB.edit(selectedEventGroup);
        init();
    }

    public void editEvent() {
        if (selectedEvent == null) {
            return;
        }
        eventEJB.edit(selectedEvent);
        init();
    }

    public void editCategorySet() {
        if (selectedCategorySet == null) {
            return;
        }
        categorySetEJB.edit(selectedCategorySet);
        init();
    }

    public void editCategory() {
        if (selectedCategory == null) {
            return;
        }
        categoryEJB.edit(selectedCategory);
        init();
    }

    public EventGroupEntity getSelectedEventGroup() {
        return selectedEventGroup;
    }

    public EventEntity getSelectedEvent() {
        return selectedEvent;
    }

    public CategorySetEntity getSelectedCategorySet() {
        return selectedCategorySet;
    }

    public CategoryEntity getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategorySet(ActionEvent event) {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        Long id = Long.parseLong(menuItem.getParams().get("categorySetId").get(0));
        selectedCategorySet = categorySetEJB.find(id);
    }

    public void setSelectedCategory(ActionEvent event) {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        Long id = Long.parseLong(menuItem.getParams().get("categoryId").get(0));
        selectedCategory = categoryEJB.find(id);
    }

    public void setSelectedEventGroup(ActionEvent event) {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        Long id = Long.parseLong(menuItem.getParams().get("eventGroupId").get(0));
        selectedEventGroup = eventGroupEJB.find(id);
    }

    public void setSelectedEvent(ActionEvent event) {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        Long id = Long.parseLong(menuItem.getParams().get("eventId").get(0));
        selectedEvent = eventEJB.find(id);
    }

    public String newObservation(ActionEvent event) {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        Long id = Long.parseLong(menuItem.getParams().get("eventId").get(0));
        sessionBean.setEventEntityForNewObservation(eventEJB.find(id));
        return "newobservation";
    }

    public void removeEventGroup() {
        selectedEventGroup.setRemoved(new Date());
        eventGroupEJB.edit(selectedEventGroup);
        init();
    }

    public void removeEvent() {
        selectedEvent.setRemoved(new Date());
        eventEJB.edit(selectedEvent);
        init();
    }

    public void removeCategorySet() {
        // Will this remove category set completely?
        categorySetEJB.remove(selectedCategorySet);
        init();
    }

    public void removeCategory() {
        // Will this remove category set completely?
        categoryEJB.remove(selectedCategory);
        init();
    }

    public void removeObservation() {
    }
}
