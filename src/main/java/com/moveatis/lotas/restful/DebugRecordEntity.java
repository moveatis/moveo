package com.moveatis.lotas.restful;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public class DebugRecordEntity {
    
    private String category;
    private Long startTime;
    private Long endTime;

    public DebugRecordEntity() {
        
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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
      
}
