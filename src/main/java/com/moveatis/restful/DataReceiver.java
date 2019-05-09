package com.moveatis.restful;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.moveatis.managedbeans.FeedbackAnalyzationManagedBean;
import com.moveatis.managedbeans.ObservationManagedBean;
import com.moveatis.managedbeans.SummaryManagedBean;

@Path("/summary")
public class DataReceiver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ObservationManagedBean observationManagedBean;

	@Inject
	private FeedbackAnalyzationManagedBean feedbackAnalyzationManagedBean;
	
	@Context
	private HttpServletRequest httpRequest;


	@POST
	@Path("image")
	@Consumes(MediaType.TEXT_PLAIN)
	public void receiveImage(String data) throws IOException {
		String[] info = data.split(",");
		if(info.length!=3)return;
		String base64Image = info[2];
		byte[] img = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		if (info[0].equals("obsimg"))
			observationManagedBean.setImage(img);
		if (info[0].equals("analpie"))
			feedbackAnalyzationManagedBean.setPieImage(img);
		if (info[0].equals("analbar"))
			feedbackAnalyzationManagedBean.setBarImage(img);
		if (info[0].equals("analtable")) 
			feedbackAnalyzationManagedBean.setTableImage(img);
		if (info[0].equals("reporttable")) 
			feedbackAnalyzationManagedBean.setReportImage(img);
	}
	
	@POST
	@Path("csv")
	@Consumes(MediaType.TEXT_PLAIN)
	public void receiveCSV(String data){
		feedbackAnalyzationManagedBean.setReportCSV(data);				
	}

}
