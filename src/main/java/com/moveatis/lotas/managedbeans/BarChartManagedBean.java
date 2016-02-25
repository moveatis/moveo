/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moveatis.lotas.managedbeans;

import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;

import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;
 
/**
 *
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
@ManagedBean
@ViewScoped  
@Named(value = "barChartBean")
public class BarChartManagedBean {
 
    private HorizontalBarChartModel barModel;
 
    @PostConstruct
    public void init() {
        createTestModel();
    }
     
    public HorizontalBarChartModel getModel() {
        return barModel;
    }
     
    private void createTestModel() {
        barModel = new HorizontalBarChartModel();
        
        String[] Categories = new String[] {"Järjestelee", "Selittää tehtävää", "Ohjaa suoritusta", "Antaa palautetta", "Tarkkailee","Oppilas suorittaa tehtävää"};
        
        Random random = new Random();
        
        for( String category : Categories) {
            ChartSeries serie = new ChartSeries();
            int count = random.nextInt(80 - 2) + 2;
            serie.setLabel(category);
            serie.set(category, count);
            barModel.addSeries(serie);
        }
        
        barModel.setLegendPosition("e");
        barModel.setAnimate(true);
        barModel.setShadow(false);
        barModel.setMouseoverHighlight(true);
        barModel.setShowDatatip(true);
        barModel.setShowPointLabels(true);
        
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Kesto %");
    }
}
