package com.moveatis.lotas.restful;

import com.moveatis.lotas.timezone.TimeZoneInformation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public class DebugObservationEntity {
    
    private List<DebugRecordEntity> records;
    private Date observationDate;
    private Long end;

    public DebugObservationEntity() {
        
    }
    
    public void addRecord(DebugRecordEntity record) {
        if(this.records == null) {
            records = new ArrayList<>();
        }
        records.add(record);
    }
    
    public List<DebugRecordEntity> getRecords() {
        return this.records;
    }
    
    public Iterator<DebugRecordEntity> getIterator() {
        return this.records.iterator();
    }

    public Date getObservationDate() {
        if(this.observationDate == null) {
            this.observationDate = Calendar.getInstance(TimeZoneInformation.getTimeZone()).getTime();
        }
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }
    
}
