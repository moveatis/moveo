/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moveatis.lotas.managedbeans;

import java.util.Date;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineGroup;
import org.primefaces.extensions.model.timeline.TimelineModel;

/**
 *
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@ManagedBean
@ViewScoped  
@Named(value = "timelineChartBean")
public class TimelineChartManagedBean {

    private TimelineModel model;  
    private String locale; // current locale as String, java.util.Locale is possible too.  
    private Date start;  
    private Date end;

    @PostConstruct  
    protected void initialize() {  
        createTestModel();
    }  

    public TimelineModel getModel() {  
        return model;  
    }  

    public String getLocale() {  
        return locale;  
    }  

    public void setLocale(String locale) {  
        this.locale = locale;  
    }  

    public Date getStart() {  
        return start;  
    }  

    public Date getEnd() {  
        return end;  
    }  

    private void createTestModel() {
        // create timeline model  
        model = new TimelineModel();
            
        Random random = new Random();
        Date now = new Date();

        String[] Categories = new String[] {"Järjestelee", "Selittää tehtävää", "Ohjaa suoritusta", "Antaa palautetta", "Tarkkailee","Oppilas suorittaa tehtävää"};
        
        for (String name : Categories) {
            Date end = new Date(now.getTime() + name.length() * 60 * 1000);
            int count = random.nextInt(50 - 5) + 5;
            
            model.addGroup(new TimelineGroup(name, name));

            for (int i = 0; i < count; i++) {  
                Date start = new Date(end.getTime() + Math.round(Math.random()) * 30 * 1000);  
                end = new Date(start.getTime() + Math.round(4 + Math.random() * 100) * 1 * 1000);  

                // create an event with content, start / end dates, editable flag, group name and custom style class  
                TimelineEvent event = new TimelineEvent("", start, end, true, name);  
                model.add(event);
            }  
        } 
    }
}  
                  