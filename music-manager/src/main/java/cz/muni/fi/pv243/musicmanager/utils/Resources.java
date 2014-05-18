package cz.muni.fi.pv243.musicmanager.utils;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resources {

	@Produces
	public Logger produceLogger(InjectionPoint injectionPoint){
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
}
