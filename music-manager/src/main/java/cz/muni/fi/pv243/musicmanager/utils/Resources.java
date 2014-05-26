package cz.muni.fi.pv243.musicmanager.utils;

import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resources {

	@Produces
	public Logger produceLogger(InjectionPoint injectionPoint){
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
	
	@Produces
	public ResourceBundle produceResourceBundle(){
		return ResourceBundle.getBundle("i18n.jsf.Messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
	}
	
	@Produces
	@RequestScoped
	public FacesContext produceFacesContext() {
		return FacesContext.getCurrentInstance();
	}
}
