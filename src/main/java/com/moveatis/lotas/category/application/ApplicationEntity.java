package com.moveatis.lotas.category.application;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Table(name="APPLICATION")
@Entity
public class ApplicationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date applicationInstalled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getApplicationInstalled() {
        return applicationInstalled;
    }

    public void setApplicationInstalled(Date applicationInstalled) {
        this.applicationInstalled = applicationInstalled;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplicationEntity)) {
            return false;
        }
        ApplicationEntity other = (ApplicationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.category.application.ApplicationEntity[ id=" + id + " ]";
    }
    
}
