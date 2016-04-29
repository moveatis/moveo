/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.export;

import com.moveatis.category.CategoryEntity;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.records.RecordEntity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ilkrpaan
 */
public class CSVFileBuilder {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVFileBuilder.class);
    
    private long totalCount;
    private long totalDuration;
    
    public CSVFileBuilder() {
        
    }
    
    public void buildCSV(OutputStream out, ObservationEntity obs, String separator) throws IOException {
        
        Long obsDuration = obs.getDuration();
        Map<CategoryEntity, CountAndDuration> countsAndDurations =
                computeCountsAndDurations(obs);
        List<RecordEntity> records = obs.getRecords();
        
        CSVBuilder csv = new CSVBuilder(out, separator);
        
        csv.add("Attribute").add("Value").newLine();
        
        csv.add("name").add(obs.getName()).newLine();
        csv.add("target").add(obs.getTarget()).newLine();
        csv.add("description").add(obs.getDescription()).newLine();
        csv.add("duration (ms)").add(obsDuration).newLine();
        csv.add("records").add(totalCount).newLine();
        
        csv.newLine();
        
        csv.add("Category").add("Count").add("Count %").add("Duration (ms)").add("Duration %").newLine();
        
        for (Map.Entry<CategoryEntity, CountAndDuration> entry : countsAndDurations.entrySet()) {
            String category = entry.getKey().getLabel().getLabel();
            CountAndDuration cnd = entry.getValue();
            long countPercent = (long)(cnd.count * 100.0 / totalCount + 0.5);
            long durationPercent = (long)(cnd.duration * 100.0 / obsDuration + 0.5);
            csv.add(category).add(cnd.count).addPercent(countPercent).add(cnd.duration).addPercent(durationPercent).newLine();
        }
        
        csv.newLine();
        
        csv.add("Category").add("Start time (ms)").add("End time (ms)").add("Duration (ms)").newLine();
        
        for (RecordEntity record : records) {
            String category = record.getCategory().getLabel().getLabel();
            Long startTime = record.getStartTime();
            Long endTime = record.getEndTime();
            csv.add(category).add(startTime).add(endTime).add(endTime - startTime).newLine();
        }
        
        csv.close();
    }
    
    private Map<CategoryEntity, CountAndDuration> computeCountsAndDurations(ObservationEntity obs) {
        
        // TODO: Categories should be in the same order as when the
        // observation was conducted.
        // Observation should contain this info, but does not yet.
        Map<CategoryEntity, CountAndDuration> countsAndDurations = new TreeMap<>(
                new Comparator<CategoryEntity>() {
                    @Override
                    public int compare(CategoryEntity c1, CategoryEntity c2) {
                        return c1.getId().compareTo(c2.getId());
                    }
                }
        );
        
        List<RecordEntity> records = obs.getRecords();
        
        totalCount = 0;
        totalDuration = 0; // NOTE: Not used anywhere.
        
        for (RecordEntity record : records) {
            CategoryEntity category = record.getCategory();
            Long deltaTime = record.getEndTime() - record.getStartTime();
            
            CountAndDuration cnd = countsAndDurations.get(category);
            if (cnd == null) {
                cnd = new CountAndDuration();
                countsAndDurations.put(category, cnd);
            }
            
            cnd.count += 1;
            cnd.duration += deltaTime;
            
            totalCount += 1;
            totalDuration += deltaTime;
        }
        
        return countsAndDurations;
    }
    
    private class CountAndDuration {
        public long count = 0;
        public long duration = 0;
    }
}
