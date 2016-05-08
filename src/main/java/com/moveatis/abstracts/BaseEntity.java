package com.moveatis.abstracts;

import com.moveatis.timezone.TimeZoneInformation;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */

@MappedSuperclass
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date created;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date removed;
    
    @PrePersist
    protected void setCreated() {
        Calendar calendar = Calendar.getInstance(TimeZoneInformation.getTimeZone());
        created = calendar.getTime();
    }
    
    protected Date getCreated() {
        return this.created;
    }
    
    protected void setRemoved() {
        Calendar calendar = Calendar.getInstance(TimeZoneInformation.getTimeZone());
        removed = calendar.getTime();
    }
    
    protected Date getRemoved() {
        return this.removed;
    }

    protected Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    
    
    
}
