package com.moveatis.lotas.scene;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.user.UserEntity;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
public class SceneEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private SceneTemplateEntity basedOn;
    
    
    @OneToMany(mappedBy = "scene")
    private Set<CategoryEntity> categories;
    
    @ManyToOne
    private UserEntity owner;

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
