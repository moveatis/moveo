package com.moveatis.lotas.variable;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.observation.CategorizedObservationEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Table(name="CATEGORIZED_VARIABLES")
@Entity
public class CategorizedVariableEntity extends AbstractVariable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private static final long serialVersionUID = 1L;
    
    @OneToOne
    private CategoryEntity category;
    
    @OneToMany(mappedBy = "observableVariable")
    private List<CategorizedObservationEntity> categorizedObservationEntitys;

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
   
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
        if (!(object instanceof CategorizedVariableEntity)) {
            return false;
        }
        CategorizedVariableEntity other = (CategorizedVariableEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.category.variable.CategorizedVatiableEntity[ id=" + id + " ]";
    }
    
}
