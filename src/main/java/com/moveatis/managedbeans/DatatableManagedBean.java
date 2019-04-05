package com.moveatis.managedbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

@ManagedBean(name = "DatatableManagedBean")
@SessionScoped
public class DatatableManagedBean {
	 
    // Init --------------------------------------------------------------------------------------
 
    private static List<List<String>> dynamicList; // Simulate fake DB.
    private static String[] dynamicHeaders; // placeholders for headers
    private HtmlPanelGroup dynamicDataTableGroup; // Placeholder.
 
    // Actions -----------------------------------------------------------------------------------
 
    private void loadDynamicList() {
 
        // Should replace these with the categorysets in use
        dynamicHeaders = new String[] {"Harry Potter", "Hermione", "Ron Weasley"};
 
        // Should replace these with selected categories
        dynamicList = new ArrayList<List<String>>();
        dynamicList.add(Arrays.asList(new String[] { "Welho", "Kaunotar", "Ginger" }));
        dynamicList.add(Arrays.asList(new String[] { "Arpi", "Kiharatukka", "Rotta" }));
        dynamicList.add(Arrays.asList(new String[] { "Pöllö", "Viisas", "Hömelö" }));
        dynamicList.add(Arrays.asList(new String[] { "Taikasauva", "Granger", "Iso suku"}));
    }
 
    private void populateDynamicDataTable() {
 
        // Context and Expression Factory
        FacesContext fCtx = FacesContext.getCurrentInstance();
        ELContext elCtx = fCtx.getELContext();
        ExpressionFactory ef = fCtx.getApplication().getExpressionFactory();
        
 
        // Create <h:dataTable value="#{datatableManagedBean.dynamicList}" var="dynamicRow">.
        HtmlDataTable dynamicDataTable = new HtmlDataTable();
        ValueExpression ve = ef.createValueExpression(elCtx,"#{DatatableManagedBean.dynamicList}",List.class);
        dynamicDataTable.setValueExpression("value", ve);
        dynamicDataTable.setVar("dynamicRow");
        
 
        // Iterate over columns
        for (int i = 0; i < dynamicList.get(0).size(); i++) {
 
            // Create <h:column>.
            HtmlColumn column = new HtmlColumn();
            dynamicDataTable.getChildren().add(column);
 
            // Create <h:outputText value="dynamicHeaders[i]"> for <f:facet name="header"> of column.
            HtmlOutputText header = new HtmlOutputText();
            header.setValue(dynamicHeaders[i]);
            column.setHeader(header);
 
            // Create <h:outputText value="#{dynamicRow[" + i + "]}"> for the body of column.
            HtmlOutputText output = new HtmlOutputText();
            ve = ef.createValueExpression(elCtx, "#{dynamicRow[" + i + "]}", String.class);
            output.setValueExpression("value", ve);
            column.getChildren().add(output);
 
        }
 
        // Add the datatable to <h:panelGroup binding="#{DatatableManagedBean.dynamicDataTableGroup}">.
        dynamicDataTableGroup = new HtmlPanelGroup();
        dynamicDataTableGroup.getChildren().add(dynamicDataTable);
 
    }
 
    // Getters -----------------------------------------------------------------------------------
 
    public HtmlPanelGroup getDynamicDataTableGroup() {
        // This will be called once in the first RESTORE VIEW phase.
        if (dynamicDataTableGroup == null) {
            loadDynamicList(); // Preload dynamic list.
            populateDynamicDataTable(); // Populate editable datatable.
        }
 
        return dynamicDataTableGroup;
    }
 
    public List<List<String>> getDynamicList() {
        return dynamicList;
    }
 
    // Setters -----------------------------------------------------------------------------------
 
    public void setDynamicDataTableGroup(HtmlPanelGroup dynamicDataTableGroup) {
        this.dynamicDataTableGroup = dynamicDataTableGroup;
    }
 
}
