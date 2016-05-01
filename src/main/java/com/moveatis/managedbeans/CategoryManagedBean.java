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
import com.moveatis.interfaces.Category;
import com.moveatis.interfaces.Label;
import com.moveatis.label.LabelEntity;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
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
@Named(value="categoryManagedBean")
@ViewScoped
public class CategoryManagedBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryManagedBean.class);
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Category categoryEJB;
    @Inject
    private Label labelEJB;
    @Inject
    private ControlManagedBean controlManagedBean;
    
    private String label;
    private String description;
    private Boolean canOverlap = false;

    /** Creates a new instance of CategoryManagedBean */
    public CategoryManagedBean() {
        
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

    public Boolean getCanOverlap() {
        return canOverlap;
    }

    public void setCanOverlap(Boolean canOverlap) {
        this.canOverlap = canOverlap;
    }

    public void addCategory(ActionEvent event) {
        LOGGER.debug("Category added");
    }
    
    public void createNewCategory(CategorySetEntity categorySetEntity) {
        CategoryEntity categoryEntity = new CategoryEntity();
        
        LabelEntity labelEntity = labelEJB.findByLabel(label);
        
        if(labelEntity == null) {
            labelEntity = new LabelEntity();
            labelEntity.setLabel(label);
            labelEJB.create(labelEntity);
        } 
        
        categoryEntity.setLabel(labelEntity);
        categoryEntity.setCategorySet(categorySetEntity);
        categoryEntity.setCreated(Calendar.getInstance().getTime());
        categoryEntity.setCanOverlap(canOverlap);
        categoryEntity.setDescription(description);
        
        Map<Integer, CategoryEntity> categories = categorySetEntity.getCategoryEntitys();
        
        if(categories == null) {
            categories = new TreeMap<>(); 
        }
        
        Integer orderNumber = categories.size();
        
        categories.put(orderNumber, categoryEntity);
        categoryEntity.setOrderNumber(orderNumber);
        categorySetEntity.setCategoryEntitys(categories);
        
        categoryEJB.create(categoryEntity);
        
        LOGGER.debug("createNewCategory, CategorySetEntity id -> " + categorySetEntity.getId());
        controlManagedBean.addCategory(categoryEntity);
        
    }
}
