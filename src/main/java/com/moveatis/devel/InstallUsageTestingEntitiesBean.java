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
package com.moveatis.devel;

import com.moveatis.category.CategoryEntity;
import com.moveatis.category.CategorySetEntity;
import com.moveatis.event.EventEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.groupkey.GroupKeyEntity;
import com.moveatis.interfaces.CategorySet;
import com.moveatis.interfaces.EventGroup;
import com.moveatis.interfaces.Session;
import com.moveatis.interfaces.TagUser;
import com.moveatis.interfaces.User;
import com.moveatis.label.LabelEntity;
import com.moveatis.user.AbstractUser;
import com.moveatis.user.IdentifiedUserEntity;
import com.moveatis.user.TagUserEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class InstallUsageTestingEntitiesBean {
    
    private GroupKeyEntity groupKey;
    private CategorySetEntity categorySet_teacher;
    private CategorySetEntity categorySet_student;
    
    private EventGroupEntity eventGroup;
    private EventEntity event;
    
    private Date createdDate;
    
    @Inject
    private Session sessionEJB;
    
    @Inject
    private EventGroup eventGroupEJB;
    
    @Inject
    private CategorySet categorySetEJB;
    
    @Inject
    private TagUser tagUserEJB;
    
    @Inject
    private User userEJB;

    public InstallUsageTestingEntitiesBean() {
        
    }
    
    public void createUsageTestingEntities() {
        
        createdDate = Calendar.getInstance().getTime();
        IdentifiedUserEntity creator = sessionEJB.getLoggedIdentifiedUser();
        
        groupKey = new GroupKeyEntity();
        groupKey.setGroupKey("avain");
        groupKey.setCreated(createdDate);
        groupKey.setCreator(creator);
        
        TagUserEntity tagUser = new TagUserEntity();
        tagUser.setGroupKey(groupKey);
        tagUser.setCreated(createdDate);
        tagUser.setCreator(creator);
        
        tagUserEJB.create(tagUser);
        
        Set<AbstractUser> users = new HashSet<>();
        users.add(tagUser);
        
        eventGroup = new EventGroupEntity();
        eventGroup.setGroupKey(groupKey);
        groupKey.setEventGroup(eventGroup);
        eventGroup.setCreated(createdDate);
        eventGroup.setOwner(creator);
        eventGroup.setUsers(users);
        eventGroup.setLabel("Budo- ja kamppailulajit");
        
        event = new EventEntity();
        event.setCreated(createdDate);
        event.setLabel("Käytettävyystestaus");
        event.setCreator(creator);
        event.setEventGroup(eventGroup);
        
        Set<EventEntity> events = new HashSet<>();
        events.add(event);
        eventGroup.setEvents(events);
        
        categorySet_teacher = new CategorySetEntity();
        categorySet_teacher.setEventGroupEntity(eventGroup);
        categorySet_teacher.setLabel("Opettajan toiminnot");
        
        categorySet_student = new CategorySetEntity();
        categorySet_student.setEventGroupEntity(eventGroup);
        categorySet_student.setLabel("Oppilaan toiminnot");
        
        List<CategoryEntity> teacherCategories = new ArrayList<>();
        
        CategoryEntity categoryEntity_1 = new CategoryEntity();
        LabelEntity labelEntity_1 = new LabelEntity();
        labelEntity_1.setLabel("Järjestelyt");
        categoryEntity_1.setLabel(labelEntity_1);
        categoryEntity_1.setCategoryGroup(categorySet_teacher);
        teacherCategories.add(categoryEntity_1);
        
        CategoryEntity categoryEntity_2 = new CategoryEntity();
        LabelEntity labelEntity_2 = new LabelEntity();
        labelEntity_2.setLabel("Tehtävän selitys");
        categoryEntity_2.setLabel(labelEntity_2);
        categoryEntity_2.setCategoryGroup(categorySet_teacher);
        teacherCategories.add(categoryEntity_2);
        
        CategoryEntity categoryEntity_3 = new CategoryEntity();
        LabelEntity labelEntity_3 = new LabelEntity();
        labelEntity_3.setLabel("Ohjaus");
        categoryEntity_3.setLabel(labelEntity_3);
        categoryEntity_3.setCategoryGroup(categorySet_teacher);
        teacherCategories.add(categoryEntity_3);
        
        CategoryEntity categoryEntity_4 = new CategoryEntity();
        LabelEntity labelEntity_4 = new LabelEntity();
        labelEntity_4.setLabel("Palautteen anto");
        categoryEntity_4.setLabel(labelEntity_4);
        categoryEntity_4.setCategoryGroup(categorySet_teacher);
        teacherCategories.add(categoryEntity_4);
        
        CategoryEntity categoryEntity_5 = new CategoryEntity();
        LabelEntity labelEntity_5 = new LabelEntity();
        labelEntity_5.setLabel("Tarkkailu");
        categoryEntity_5.setLabel(labelEntity_5);
        categoryEntity_5.setCategoryGroup(categorySet_teacher);
        teacherCategories.add(categoryEntity_5);
        
        CategoryEntity categoryEntity_6 = new CategoryEntity();
        LabelEntity labelEntity_6 = new LabelEntity();
        labelEntity_6.setLabel("Muu toiminta");
        categoryEntity_6.setLabel(labelEntity_1);
        categoryEntity_6.setCategoryGroup(categorySet_teacher);
        teacherCategories.add(categoryEntity_6);
        
        categorySet_teacher.setCategoryEntitys(teacherCategories);
       
        List<CategoryEntity> studentCategories = new ArrayList<>();
        
        CategoryEntity studentEntity_1 = new CategoryEntity();
        LabelEntity studentLabel_1 = new LabelEntity();
        studentLabel_1.setLabel("Oppilas suorittaa tehtävää");
        studentEntity_1.setLabel(labelEntity_6);
        studentEntity_1.setCategoryGroup(categorySet_student);
        studentCategories.add(studentEntity_1);
        
        categorySet_student.setCategoryEntitys(studentCategories);
       
        Set<CategorySetEntity> categorySets = new HashSet<>();
        categorySets.add(categorySet_teacher);
        categorySets.add(categorySet_student);
        
        eventGroup.setCategoryGroups(categorySets);
        
        categorySetEJB.create(categorySet_teacher);
        categorySetEJB.create(categorySet_student);
        eventGroupEJB.create(eventGroup);
        
    }
}
