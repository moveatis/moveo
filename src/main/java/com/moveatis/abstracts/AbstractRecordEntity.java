package com.moveatis.abstracts;

import java.io.File;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.moveatis.observation.ObservationCategory;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.records.RecordEntity;

@MappedSuperclass
public abstract class AbstractRecordEntity extends BaseEntity {
    
    
    private Long startTime;
    private Long endTime;
    
    
    private String comment;


    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RecordEntity)) {
            return false;
        }
        RecordEntity other = (RecordEntity) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.moveatis.records.RecordEntity[ id=" + id + " ]";
    }
    
}
