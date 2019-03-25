package com.moveatis.abstracts;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.moveatis.event.EventEntity;
import com.moveatis.observation.ObservationCategorySet;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.records.RecordEntity;
import com.moveatis.user.AbstractUser;

@MappedSuperclass
public abstract class AbstractObservationEntity extends BaseEntity{

	    @ManyToOne
	    private AbstractUser observer;

	    @ManyToOne
	    private EventEntity event;



	    private long duration;

	    private String description;
	    private String name;
	    private String target;
	    private Boolean userWantsToSaveToDatabase;

	    @Override
	    public Long getId() {
	        return id;
	    }

	    @Override
	    public void setId(Long id) {
	        this.id = id;
	    }


	    public AbstractUser getObserver() {
	        return observer;
	    }

	    public void setObserver(AbstractUser observer) {
	        this.observer = observer;
	    }

	    public EventEntity getEvent() {
	        return event;
	    }

	    public void setEvent(EventEntity event) {
	        this.event = event;
	    }


	    public long getDuration() {
	        return duration;
	    }

	    public void setDuration(long duration) {
	        this.duration = duration;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getTarget() {
	        return target;
	    }

	    public void setTarget(String target) {
	        this.target = target;
	    }



	    public Boolean getUserWantsToSaveToDatabase() {
	        return userWantsToSaveToDatabase;
	    }

	    public void setUserWantsToSaveToDatabase(Boolean userWantsToSaveToDatabase) {
	        this.userWantsToSaveToDatabase = userWantsToSaveToDatabase;
	    }

	    @Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (id != null ? id.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        if (!(object instanceof ObservationEntity)) {
	            return false;
	        }
	        ObservationEntity other = (ObservationEntity) object;
	        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	    }

	    @Override
	    public String toString() {
	        return "com.moveatis.observation.ObservationEntity[ id=" + id + " ]";
	    }

}
