package com.moveatis.lotas.event;

import com.moveatis.lotas.category.CategoryGroupEntity;
import com.moveatis.lotas.user.AbstractUser;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
@Table(name="EVENTGROUPS")
public class EventGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private AbstractUser owner;
    @ManyToOne
    private AbstractUser user;
    
    @OneToMany(mappedBy = "sceneGroup")
    private Set<CategoryGroupEntity> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public AbstractUser getOwner() {
        return owner;
    }

    public void setOwner(AbstractUser owner) {
        this.owner = owner;
    }

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventGroupEntity)) {
            return false;
        }
        EventGroupEntity other = (EventGroupEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.scene.SceneTemplate[ id=" + id + " ]";
    }
    
}
