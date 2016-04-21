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
package com.moveatis.event;

import com.moveatis.category.CategorySetEntity;
import com.moveatis.groupkey.GroupKeyEntity;
import com.moveatis.user.AbstractUser;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
@NamedQueries({
        @NamedQuery(
            name="findEventGroupByOwner",
            query="SELECT eventGroup FROM EventGroupEntity eventGroup WHERE eventGroup.owner=:ownerEntity"
        )
})
@Table(name="EVENTGROUP")
public class EventGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy = "eventGroupEntity", cascade=PERSIST)
    private Set<CategorySetEntity> categoryGroups;
    
    @OneToMany(mappedBy = "eventGroup", cascade=PERSIST, fetch=EAGER)
    private Set<EventEntity> events;
    
    @OneToOne(cascade=PERSIST)
    private GroupKeyEntity groupKey;
    
    @ManyToOne
    private AbstractUser owner;
    
    @OneToMany
    private Set<AbstractUser> users;
    
    private String label;
    private String description;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date removed;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbstractUser getOwner() {
        return owner;
    }

    public void setOwner(AbstractUser owner) {
        this.owner = owner;
    }

    public Set<AbstractUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AbstractUser> users) {
        this.users = users;
    }

    public Set<CategorySetEntity> getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(Set<CategorySetEntity> categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getRemoved() {
        return removed;
    }

    public void setRemoved(Date removed) {
        this.removed = removed;
    }

    public Set<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(Set<EventEntity> events) {
        this.events = events;
    }

    public GroupKeyEntity getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(GroupKeyEntity groupKey) {
        this.groupKey = groupKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EventGroupEntity)) {
            return false;
        }
        EventGroupEntity other = (EventGroupEntity) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.scene.EventGroupEntity[ id=" + id + " ]";
    }
    
}
