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
 
package com.moveatis.identityprovider;

import com.moveatis.abstracts.BaseEntity;
import com.moveatis.user.IdentifiedUserEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This entity represents the data needed to identify a existing user. This is 
 * an example entity, which must be rewritten, if custom identityservice is to be used.
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
@NamedQueries({
        @NamedQuery(
            name="findIdentityProviderEntityByUsername",
            query="SELECT entity FROM IdentityProviderInformationEntity entity WHERE "
                    + "entity.username=:username"
        )
})
@Table(name="IDENTITY_PROVIDER_INFORMATION")
public class IdentityProviderInformationEntity extends BaseEntity implements IdentityProvider, Serializable {
    
    private static final long serialVersionUID = 1L;

    @OneToOne
    private IdentifiedUserEntity userEntity;

    private String username;
    private String affiliation;
    
    public IdentityProviderInformationEntity() {
        
    }

    public void setUserEntity(IdentifiedUserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public IdentifiedUserEntity getIdentifiedUserEntity() {
        return this.userEntity;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }
}