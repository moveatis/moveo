package com.moveatis.abstracts;


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

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.abstracts.BaseEntity;
import com.moveatis.event.EventGroupEntity;
import com.moveatis.user.IdentifiedUserEntity;
import java.io.Serializable;
import java.util.Map;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

	/**
	 * The entity represents a category set in the database.
	 * @author Sami Kallio <phinaliumz at outlook.com>
	 */
@MappedSuperclass
public abstract class AbstractCategorySetEntity extends BaseEntity implements Serializable {
	    
	    private static final long serialVersionUID = 1L;
	    
	    @ManyToOne
	    private IdentifiedUserEntity creator;
	    
	    @ManyToOne
	    private EventGroupEntity eventGroupEntity;
	    
	    private String label;
	    private String description;
	    

		@Override
	    public Long getId() {
	        return id;
	    }

	    @Override
	    public void setId(Long id) {
	        this.id = id;
	    }


	    public IdentifiedUserEntity getCreator() {
	        return creator;
	    }

	    public void setCreator(IdentifiedUserEntity creator) {
	        this.creator = creator;
	    }

	    public EventGroupEntity getEventGroupEntity() {
	        return eventGroupEntity;
	    }

	    public void setEventGroupEntity(EventGroupEntity eventGroupEntity) {
	        this.eventGroupEntity = eventGroupEntity;
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

	    @Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (id != null ? id.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        if (!(object instanceof AbstractCategorySetEntity)) {
	            return false;
	        }
	        AbstractCategorySetEntity other = (AbstractCategorySetEntity) object;
	        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	    }

	    @Override
	    public String toString() {
	        return "com.moveatis.category.CategoryTemplateEntity[ id=" + id + " ]";
	    }

		public abstract Map<Integer, AbstractCategoryEntity> getCategoryEntitys();

		public abstract void setCategoryEntitys(Map<Integer, AbstractCategoryEntity> categories);
	    
	}


