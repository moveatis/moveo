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

import com.moveatis.managedbeans.FeedbackAnalysisManagedBean;
import com.moveatis.managedbeans.ObservationManagedBean;

@Path("/summary")
public class DataReceiver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ObservationManagedBean observationManagedBean;

	@Inject
	private FeedbackAnalysisManagedBean feedbackAnalysisManagedBean;
	
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
			feedbackAnalysisManagedBean.setPieImage(img);
		if (info[0].equals("analbar"))
			feedbackAnalysisManagedBean.setBarImage(img);
		if (info[0].equals("analtable")) 
			feedbackAnalysisManagedBean.setTableImage(img);
		if (info[0].equals("reporttable")) 
			feedbackAnalysisManagedBean.setReportImage(img);
	}
	
	@POST
	@Path("csv")
	@Consumes(MediaType.TEXT_PLAIN)
	public void receiveCSV(String data){
		feedbackAnalysisManagedBean.setReportCSV(data);
	}

}
