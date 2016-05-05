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

import com.moveatis.helpers.Validation;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 * @author Ilari Paananen <ilari.k.paananen at student.jyu.fi>
 */
public class ObservationCategory {
    
    private long id;
    private long type;
    private String name;
    private boolean inDatabase;

    public ObservationCategory() {
        this.id = 0l;
        this.type = 0l;
        this.name = "";
        this.inDatabase = false;
    }

    public ObservationCategory(Long id, String name) {
        this.id = id;
        this.type = 0l;
        this.name = name;
        this.inDatabase = true;
    }

    public ObservationCategory(ObservationCategory other) {
        this.id = other.id;
        this.type = other.type;
        this.name = other.name;
        this.inDatabase = other.inDatabase;
        // NOTE: inDatabase should always be true when cloning other category!
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public String getName() {
         return name;
    }

    public final void setName(String name) {
        String validName = Validation.validateForJsAndHtml(name).trim();
        if (!this.name.equals(validName)) {
            this.name = validName;
            // If the name is edited, it's not anymore in the database.
            this.inDatabase = false;
        }
    }

    public Boolean isInDatabase() {
        return inDatabase;
    }

    public void setInDatabase(boolean inDatabase) {
        this.inDatabase = inDatabase;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        ObservationCategory category = (ObservationCategory)o;
        return name.equals(category.name);
    }

}
