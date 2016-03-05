package com.moveatis.lotas.category;

import com.moveatis.lotas.scene.SceneGroupEntity;
import com.moveatis.lotas.scenekey.SceneKeyEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Entity
@Table(name="CATEGORYGROUPS")
public class CategoryGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy = "categoryGroup")
    private List<CategoryEntity> categoryEntitys;

    @OneToOne
    private SceneKeyEntity sceneKey;
    
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
        if (!(object instanceof CategoryGroupEntity)) {
            return false;
        }
        CategoryGroupEntity other = (CategoryGroupEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.category.CategoryTemplateEntity[ id=" + id + " ]";
    }
    
}
