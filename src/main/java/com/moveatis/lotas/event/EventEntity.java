package com.moveatis.lotas.event;

import com.moveatis.lotas.eventkey.EventKeyEntity;
import com.moveatis.lotas.records.RecordEntity;
import com.moveatis.lotas.user.AbstractUser;
import com.moveatis.lotas.user.UserEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
@Table(name="EVENTS")
@NamedQueries(
        @NamedQuery(name="EventEntity.findByUser", query="SELECT event FROM EventEntity event WHERE event.owner = :user")
)
public class EventEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private EventGroupEntity basedOn;
    
    @NotNull
    @ManyToOne
    private AbstractUser owner;
        
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date created;
    
    @OneToOne(mappedBy = "eventEntity")
    private EventKeyEntity eventKeyEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventGroupEntity getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(EventGroupEntity basedOn) {
        this.basedOn = basedOn;
    }

    public AbstractUser getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
    public EventKeyEntity getEventKeyEntity() {
        return eventKeyEntity;
    }

    public void setEventKeyEntity(EventKeyEntity eventKeyEntity) {
        this.eventKeyEntity = eventKeyEntity;
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
        if (!(object instanceof EventEntity)) {
            return false;
        }
        EventEntity other = (EventEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.scene.Scene[ id=" + id + " ]";
    }
    
}
