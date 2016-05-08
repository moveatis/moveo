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
import com.moveatis.event.EventGroupEntity;
import com.moveatis.interfaces.Category;
import com.moveatis.interfaces.CategorySet;
import com.moveatis.interfaces.Label;
import com.moveatis.interfaces.Session;
import com.moveatis.label.LabelEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashSet;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Named(value = "categorySetManagedBean")
@ViewScoped
public class CategorySetManagedBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategorySetManagedBean.class);

    private final static long serialVersionUID = 1L;

    private String name;
    private String description;

    @Inject
    private ControlManagedBean controlManagedBean;
    @Inject
    private Session sessionBean;
    @Inject
    private CategorySet categorySetEJB;
    @Inject
    private Category categoryEJB;
    @Inject
    private Label labelEJB;

    private CategorySetEntity categorySetEntity;

    /**
     * Creates a new instance of CategorySetManagedBean
     */
    public CategorySetManagedBean() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void createNewCategorySet(EventGroupEntity eventGroupEntity) {
        categorySetEntity = new CategorySetEntity();
        categorySetEntity.setCreator(sessionBean.getLoggedIdentifiedUser());
        categorySetEntity.setEventGroupEntity(eventGroupEntity);
        categorySetEntity.setLabel(name);
        categorySetEntity.setDescription(description);

        Set<CategorySetEntity> categorySets = eventGroupEntity.getCategorySets();

        if (categorySets == null) {
            categorySets = new HashSet<>();
        }

        categorySets.add(categorySetEntity);
        eventGroupEntity.setCategorySets(categorySets);

        categorySetEJB.create(categorySetEntity);

        //controlManagedBean.addCategorySet(categorySetEntity);
    }

    public void createNewCategorySet(EventGroupEntity eventGroupEntity,
            CategorySetEntity categorySetEntity, List<CategoryEntity> categories) {

        Map<Integer, CategoryEntity> categoriesOrdered = categorySetEntity.getCategoryEntitys();
        if(categoriesOrdered == null) {
            categoriesOrdered = new TreeMap<>();
        }
        List<CategoryEntity> unordered = new ArrayList<>();

        if (categoriesOrdered == null) {
            categoriesOrdered = new TreeMap<>();
        }
        
        for (CategoryEntity categoryEntity : categories) {
            String label = categoryEntity.getLabel().getLabel();
            LabelEntity labelEntity = labelEJB.findByLabel(label);

            if (labelEntity == null) {
                labelEntity = new LabelEntity();
                labelEntity.setLabel(label);
                labelEJB.create(labelEntity);
            }

            categoryEntity.setLabel(labelEntity);
            categoryEntity.setCategorySet(categorySetEntity);

            if (categoryEntity.getOrderNumber() == null) {
                unordered.add(categoryEntity);
            } else {
                categoriesOrdered.put(categoryEntity.getOrderNumber(), categoryEntity);
            }

            if (categoryEntity.getId() != null) {
                categoryEJB.edit(categoryEntity);
            } 
        }

        for (CategoryEntity categoryEntity : unordered) {
            categoryEntity.setOrderNumber(categoriesOrdered.size());
            categoriesOrdered.put(categoriesOrdered.size(), categoryEntity);
            categoryEJB.edit(categoryEntity);
        }

        categorySetEntity.setCategoryEntitys(categoriesOrdered);
        categorySetEntity.setCreator(sessionBean.getLoggedIdentifiedUser());
        categorySetEntity.setEventGroupEntity(eventGroupEntity);

        Set<CategorySetEntity> categorySets = eventGroupEntity.getCategorySets();

        if (categorySets == null) {
            categorySets = new HashSet<>();
        }

        categorySets.add(categorySetEntity);
        eventGroupEntity.setCategorySets(categorySets);
        
        if (categorySetEntity.getId() == null) {
            categorySetEJB.create(categorySetEntity);
        } else {
            categorySetEJB.edit(categorySetEntity);
        }
    }
}
