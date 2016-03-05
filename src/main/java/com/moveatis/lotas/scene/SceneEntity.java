package com.moveatis.lotas.scene;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.scenekey.SceneKeyEntity;
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
@Table(name="SCENES")
@NamedQueries(
        @NamedQuery(name="SceneEntity.findByUser", query="SELECT scene FROM SceneEntity scene WHERE scene.owner = :user")
)
public class SceneEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private SceneGroupEntity basedOn;
    
    @OneToMany(mappedBy = "scene")
    private Set<CategoryEntity> categories;
    
    @NotNull
    @ManyToOne
    private AbstractUser owner;
        
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date created;
    
    @OneToOne(mappedBy = "sceneEntity")
    private SceneKeyEntity sceneKeyEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SceneGroupEntity getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(SceneGroupEntity basedOn) {
        this.basedOn = basedOn;
    }

    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
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
    
    public SceneKeyEntity getSceneKeyEntity() {
        return sceneKeyEntity;
    }

    public void setSceneKeyEntity(SceneKeyEntity sceneKeyEntity) {
        this.sceneKeyEntity = sceneKeyEntity;
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
        if (!(object instanceof SceneEntity)) {
            return false;
        }
        SceneEntity other = (SceneEntity) object;
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
