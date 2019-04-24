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
package com.moveatis.category;

import com.moveatis.abstracts.AbstractCategoryEntity;
import com.moveatis.abstracts.AbstractCategorySetEntity;
import com.moveatis.abstracts.BaseEntity;
import com.moveatis.label.LabelEntity;
import java.io.Serializable;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The entity represents the categories of an observation in the database.
 * 
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Table(name = "CATEGORY")
@Entity
public class CategoryEntity extends AbstractCategoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean canOverlap;
	private CategoryType categoryType;

	@ManyToOne
	private CategorySetEntity categorySet;

	public Boolean getCanOverlap() {
		return canOverlap;
	}

	public void setCanOverlap(Boolean canOverlap) {
		this.canOverlap = canOverlap;
	}

	public CategoryType getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(CategoryType categoryType) {
		this.categoryType = categoryType;
	}

	@Override
	public void setCategorySet(AbstractCategorySetEntity categorySetEntity) {
		this.categorySet = (CategorySetEntity) categorySetEntity;

	}

	@Override
	public CategorySetEntity getCategorySet() {
		return this.categorySet;
	}

}
