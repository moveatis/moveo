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
package com.moveatis.managedbeans;

import com.moveatis.export.CSVFileBuilder;
import com.moveatis.interfaces.Observation;
import com.moveatis.interfaces.Session;
import com.moveatis.observation.ObservationEntity;
import com.moveatis.records.RecordEntity;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for Summary page managed bean. Responsive for creating summary page
 * timeline model and get required variables from observation.
 *
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@Named(value = "summaryBean")
@RequestScoped
public class SummaryManagedBean {
    
    private TimelineModel timeline;
    private final Date min;
    private final Date start;
    private final long zoomMin;
    private final long zoomMax;
    private Date max;

    private ObservationEntity observation;

    private String saveOption;

    @Inject
    private Observation observationEJB; //EJB-beans have EJB in their name by convention

    //@Inject
    //private CategorySelectionManagedBean categoryBean; //Managedbeans have Bean in their name by convention
    
    @Inject
    private Session sessionBean;

    @Inject
    private ValidationManagedBean validationBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(SummaryManagedBean.class);

    /**
     * Default constructor to initialize timeline options.
     */
    public SummaryManagedBean() {
        this.start = new Date(0);
        this.min = new Date(0);
        this.max = new Date(0);
        this.zoomMin = 10 * 1000;
        this.zoomMax = 24 * 60 * 60 * 1000;
        this.saveOption = "mail";
    }

    /**
     * Post constructor to create timeline on request.
     */
    @PostConstruct
    protected void initialize() {
        createTimeline();
    }

    /**
     * Get timeline model.
     *
     * @return TimelineModel
     */
    public TimelineModel getTimeline() {
        return timeline;
    }

    /**
     * Get min date of timeline. User cannot move the timeline before that date.
     *
     * @return Date
     */
    public Date getMin() {
        return min;
    }

    /**
     * Get maximum date of timeline. User cannot move the timeline after that
     * date.
     *
     * @return Date
     */
    public Date getMax() {
        return max;
    }

    /**
     * Get timeline start date.
     *
     * @return Date
     */
    public Date getStart() {
        return start;
    }

    /**
     * Get minimum zoom interval for timeline in milliseconds.
     *
     * @return long
     */
    public long getZoomMin() {
        return zoomMin;
    }

    /**
     * Get maximum zoom interval for timeline in milliseconds.
     *
     * @return long
     */
    public long getZoomMax() {
        return zoomMax;
    }

    public ObservationEntity getObservation() {
        return observation;
    }

    public void setObservation(ObservationEntity observation) {
        this.observation = observation;
    }

    public String getSaveOption() {
        return saveOption;
    }

    public void setSaveOption(String saveOption) {
        this.saveOption = saveOption;
    }

    /**
     * Create timeline model. Add category groups as timeline event groups and
     * records as timeline events.
     */
    private void createTimeline() {
        timeline = new TimelineModel();

        SortedSet<Long> observations = sessionBean.getSessionObservationsIds();
        
        if(observations.isEmpty()) {
            return;
        }
        
        /*
        * TODO: SortedSet offers first-method to return first element in the set
        * What if there are more observations?
        */
        Long observationId = observations.last();

        observation = observationEJB.find(observationId);
        
        CSVFileBuilder csv = new CSVFileBuilder();
//        csv.printCSV(observation); // Doesn't work... maybe records etc. need to be get from EJB?
        csv.printDummyCSV(); // Print some dummy csv data.

        List<RecordEntity> records = observationEJB.findRecords(observationId);
        LOGGER.debug("Records-size ->" + records.size());
        //List<CategorySet> categorySets = categoryBean.getCategorySetsInUse();

        this.max = new Date(Math.round(this.observation.getDuration() * 1.1)); // timeline max 110% of obs. duration

        // Add categories to timeline as timelinegroups
        int categoryNumber = 1;
//        for (CategorySet categorySet : categorySets) {
//            for (String category : categorySet.getSelectedCategories()) {
//                // Add category name inside element with class name
//                // use css style to hide them in timeline
//                // class name is intentionally without quotes, timeline expectional case
//                String numberedLabel = "<span class=categoryNumber>" + categoryNumber + ". </span>"
//                        + "<span class=categoryLabel>" + StringEscapeUtils.escapeHtml4(category) + "</span>";
//                TimelineGroup timelineGroup = new TimelineGroup(category, numberedLabel);
//                timeline.addGroup(timelineGroup);
//                // Add dummy records to show empty categories in timeline
//                TimelineEvent timelineEvent = new TimelineEvent("", new Date(0), false, category, "dummyRecord");
//                timeline.add(timelineEvent);
//                categoryNumber++;
//            }
//        }

        // Add records to timeline as timeline-events
        for (RecordEntity record : records) {
            String category = record.getCategory().getLabel().getLabel();
            long startTime = record.getStartTime();
            long endTime = record.getEndTime();
            TimelineEvent timelineEvent = new TimelineEvent("", new Date(startTime),
                    new Date(endTime), false, category);
            timeline.add(timelineEvent);
        }
    }

    public void saveCurrentObservation() {
        observationEJB.edit(observation);
    }

    public void sendCurrentObservation() {
        // 
    }

    public void downloadCurrentObservation() {
        //
    }

    public boolean sendOptionSelected() {
        return this.saveOption.equals("mail");
    }

    public boolean saveOptionSelected() {
        return this.saveOption.equals("save");
    }

    public boolean downloadOptionSelected() {
        return this.saveOption.equals("download");
    }
}
