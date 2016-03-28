
package com.moveatis.lotas.category;

import com.moveatis.lotas.event.EventEntity;
import com.moveatis.lotas.records.RecordEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Table(name="CATEGORIES")
@Entity
@NamedQuery(name="Category.findByLabel", query="SELECT category FROM CategoryEntity category WHERE category.label = :label")
public class CategoryEntity implements Serializable {

    @OneToMany(mappedBy = "category")
    private List<RecordEntity> records;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date created;
    
    @NotNull
    private String label;
    
    @ManyToOne
    private CategoryGroupEntity categoryGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<RecordEntity> getRecords() {
        return records;
    }

    public void setRecords(List<RecordEntity> records) {
        this.records = records;
    }

    public CategoryGroupEntity getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(CategoryGroupEntity categoryGroup) {
        this.categoryGroup = categoryGroup;
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
        if (!(object instanceof CategoryEntity)) {
            return false;
        }
        CategoryEntity other = (CategoryEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.moveatis.lotas.category.Category[ id=" + id + " ]";
    }
    
}
