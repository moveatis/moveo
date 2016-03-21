package com.moveatis.lotas.observation;

import com.moveatis.lotas.records.RecordEntity;
import com.moveatis.lotas.event.EventEntity;
import com.moveatis.lotas.user.UserEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.>
 */
@Table(name="OBSERVATIONS")
@Entity
public class ObservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private UserEntity user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @OneToMany(mappedBy = "observation")
    private List<RecordEntity> records;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RecordEntity> getRecords() {
        return records;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setRecords(List<RecordEntity> records) {
        this.records = records;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
        if (!(object instanceof ObservationEntity)) {
            return false;
        }
        ObservationEntity other = (ObservationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.observation.ObservationEntity[ id=" + id + " ]";
    }
    
}
