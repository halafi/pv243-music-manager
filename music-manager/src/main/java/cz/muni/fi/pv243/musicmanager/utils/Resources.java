package cz.muni.fi.pv243.musicmanager.utils;

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
	@RequestScoped
	public FacesContext produceFacesContext() {
		return FacesContext.getCurrentInstance();
	}
}
