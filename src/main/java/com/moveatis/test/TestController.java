package com.moveatis.test;

import javax.inject.Inject;
import javax.inject.Named;

import com.moveatis.application.RedirectURLs;
import com.moveatis.interfaces.Session;
import com.moveatis.session.SessionBean;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Named(value = "testController")
@ManagedBean
public class TestController {
	

	public String showHello()
	{
		return "Hello from the other side";
	}
	
	public String redirectObservation()
	{
		return "observationselected";		
	}
	
	public String redirectAnalyze()
	{
		return "analysisselected";		
	}
}
