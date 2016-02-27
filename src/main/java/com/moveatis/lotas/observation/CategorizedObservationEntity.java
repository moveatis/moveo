package com.moveatis.lotas.observation;

import com.moveatis.lotas.category.CategoryEntity;
import com.moveatis.lotas.variable.AbstractVariable;
import com.moveatis.lotas.variable.CategorizedVariableEntity;
import com.moveatis.lotas.scene.SceneEntity;
import com.moveatis.lotas.user.UserEntity;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.>
 */
@Table(name="OBSERVATIONS")
@Entity
public class CategorizedObservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Temporal(TemporalType.TIME)
    private Date endTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @ManyToOne
    private UserEntity user;
    
    @ManyToOne
    private CategorizedVariableEntity observableVariable;
    
    @ManyToOne
    private SceneEntity scene;

    public CategorizedVariableEntity getObservableVariable() {
        return observableVariable;
    }

    public void setObservableVariable(CategorizedVariableEntity observableVariable) {
        this.observableVariable = observableVariable;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public SceneEntity getScene() {
        return scene;
    }

    public void setScene(SceneEntity scene) {
        this.scene = scene;
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
        if (!(object instanceof CategorizedObservationEntity)) {
            return false;
        }
        CategorizedObservationEntity other = (CategorizedObservationEntity) object;
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
