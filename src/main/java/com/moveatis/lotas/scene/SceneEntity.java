package com.moveatis.lotas.scene;

import com.moveatis.lotas.category.CategoryEntity;
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
    private SceneTemplateEntity basedOn;
    
    @OneToMany(mappedBy = "scene")
    private Set<CategoryEntity> categories;
    
    @NotNull
    @ManyToOne
    private UserEntity owner;
    
    @NotNull
    private String tag;
    
    @NotNull
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SceneTemplateEntity getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(SceneTemplateEntity basedOn) {
        this.basedOn = basedOn;
    }

    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
