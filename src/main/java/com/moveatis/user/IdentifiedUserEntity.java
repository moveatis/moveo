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
package com.moveatis.user;

import static javax.persistence.CascadeType.PERSIST;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.moveatis.identityprovider.IdentityProviderInformationEntity;

/**
 * The entity represents the individual user, which is identified using the
 * Shibboleth identity system of Jyväskylä University.
 * 
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "findUserByName", query = "SELECT user FROM IdentifiedUserEntity user WHERE user.givenName=:givenName") })
@Table(name = "IDENTIFIED_USER")
public class IdentifiedUserEntity extends AbstractUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne(mappedBy = "userEntity", cascade = PERSIST)
	private IdentityProviderInformationEntity identityProviderInformation;

	private String givenName;
	private String email;

	public IdentityProviderInformationEntity getIdentityProviderInformation() {
		return identityProviderInformation;
	}

	public void setIdentityProviderInformation(IdentityProviderInformationEntity identityProviderInformation) {
		this.identityProviderInformation = identityProviderInformation;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public IdentityProviderInformationEntity getIdentityProviderInformationEntity() {
		return identityProviderInformation;
	}

	public void setIdentityProviderInformationEntity(IdentityProviderInformationEntity identityProviderInformation) {
		this.identityProviderInformation = identityProviderInformation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof IdentifiedUserEntity)) {
			return false;
		}
		IdentifiedUserEntity other = (IdentifiedUserEntity) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "com.moveatis.user.IdentifiedUserEntity[ id=" + id + " ]";
	}
}
