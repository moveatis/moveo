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

package com.moveatis.observation;

import com.moveatis.category.CategoryType;
import com.moveatis.helpers.Validation;
import java.io.Serializable;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Ilari Paananen <ilari.k.paananen at student.jyu.fi>
 */
public class ObservationCategory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final CategoryType type;
    private Long tag;
    private String name;

    public ObservationCategory() {
        this.type = CategoryType.TIMED;
        this.name = "";
    }

    public ObservationCategory(Long tag, String name) {
        this.type = CategoryType.TIMED;
        this.name = name;
        this.tag = tag;
    }

    public ObservationCategory(ObservationCategory other) {
        this.type = other.type;
        this.name = other.name;
        this.tag = other.tag;
    }
    
    public ObservationCategory(CategoryType categoryType, Long tag, String name) {
        this.type = categoryType;
        this.tag = tag;
        this.name = name;
    }

    public CategoryType getType() {
        return type;
    }

    public int getTypeAsInt() {
        return type.ordinal();
    }
    
    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public String getName() {
         return name;
    }

    public final void setName(String name) {
        String validName = Validation.validateForJsAndHtml(name).trim();
        if (!this.name.equals(validName)) {
            this.name = validName;
        }
    }
}
