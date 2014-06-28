package cz.muni.fi.pv243.musicmanager.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.entities.Interpret.Genre;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.InterpretService;

/**
 * Interpret JSF controller for the {@link Interpret} entity.
 * 
 * @author Roman Macor
 */

@Model
public class InterpretController {

	@Inject
	private FacesContext facesContext;

	@Inject
	private ResourceBundle bundle;

	@Inject
	private InterpretService interpretService;

	@Inject
	private Logger logger;

	@Produces
	@Named
	private Interpret interpret;
	
	@PostConstruct
	public void initInterpret() {
		interpret = new Interpret();
	}

	public List<Interpret> getAllInterprets() {
		try {
			// return songService.getSongsbyInterpret("Unknown");
			return interpretService.getAllInterprets();
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("interpret.getAll.fail.service"), null));
		}
		return null;
	}

	public String addInterpret() {
		try {
			interpretService.createInterpret(interpret);

			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("interpret.add.success"), null));
			logger.info(interpret.getName() + " created ");
			initInterpret();
			return "index";
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("interpret.add.fail.service"), null));
			return "";
		}
	}

	public String updateInterpret() {
		try {
			interpretService.updateInterpret(interpret);
			
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("interpret.update.success"), null));
			logger.info(interpret.getName() + " created ");
			initInterpret();
			return "index";
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("interpret.update.fail.service"), null));
			return "";
		}
	}
	
	public void removeInterpret(String id) {
		try {
			Interpret interpret = interpretService.getInterpretById(id);
			interpretService.deleteInterpret(interpret);

			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("interpret.remove.success"), null));
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("interpret.remove.fail.service"), null));
		}
	}

	public void removeAllInterprets() {
		try {
			interpretService.removeAllInterprets();
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("interpret.remove.success"), null));
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("interpret.remove.fail.service"), null));
		}
	}

	public String editInterpret(String id) {
		try {
			interpret = interpretService.getInterpretById(id);
		} catch (ServiceException e) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("interpret.get.fail.service"), null));
		}
		return "edit";
	}

	public Genre[] getGenres() {
		return Genre.values();

	}

}
