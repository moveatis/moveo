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
package com.moveatis.lotas.application;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.enums.ApplicationStatusCode;
import com.moveatis.lotas.interfaces.Application;
import com.moveatis.lotas.interfaces.User;
import com.moveatis.lotas.user.UserEntity;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
public class InstallDevelEntitiesBean {
    
    @Inject
    private Application applicationEJB;
    
    @Inject
    private User userEJB;
    
    public InstallDevelEntitiesBean() {
        
    }
    
    public ApplicationStatusCode createDevelEntities() {
        
        if(applicationEJB.find(1L) == null) {
            ApplicationEntity applicationEntity = new ApplicationEntity();
            applicationEntity.setApplicationInstalled(Calendar.getInstance().getTime());
            applicationEJB.create(applicationEntity);
            
            if(applicationEJB.find(1L) == null) {
                return ApplicationStatusCode.INSTALLATION_FAILED;
            }
        }
        
        userEJB.create(createRegularUser());
        UserEntity superUser = createAdminUser();
        userEJB.create(superUser);
        applicationEJB.addSuperUser(superUser);
        
        return ApplicationStatusCode.INSTALLATION_OK;
    }
    
    private UserEntity createRegularUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("Taavi");
        user.setLastName("Testaaja");
        return user;
    }

    private UserEntity createAdminUser() {
        UserEntity admin = new UserEntity();
        admin.setFirstName("Paavo");
        admin.setLastName("Paakayttaja");
        return admin;
    }
    
    private CategoryEntity createCategory() {
        CategoryEntity category = new CategoryEntity();
        return category;
    }
}
