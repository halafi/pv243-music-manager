package cz.muni.fi.pv243.musicmanager.controller;


import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.IdentityType;
import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.entities.User;
import cz.muni.fi.pv243.musicmanager.services.UserService;

@Model
public class UserController {

	@Inject
	private FacesContext facesContext;
	
	@Inject
	private UserService userService;
	
	@Inject
	private Logger logger;
	
	@Inject
	private IdentityManager im;
	
	@Produces
	@Named
	private User newUser;
	
	@PostConstruct
	public void initNewUser() {
		newUser = new User();
	}
	
	public void register(){
		try {
			userService.createUser(newUser);
		
			Password pass = new Password(newUser.getPassword());	
			org.picketlink.idm.model.basic.User user = new org.picketlink.idm.model.basic.User(newUser.getUsername());
			
			im.add(user);
			im.updateCredential(user, pass);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
												"Registered",
												"Registration successful");
			facesContext.addMessage(null, m);
			initNewUser();
		}catch (Exception e) { 
			String errorMessage = getRootErrorMessage(e); 
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful"); 
			facesContext.addMessage(null, m); 
			}
		}
	private String getRootErrorMessage(Exception e) {
		String errorMessage = "Registration failed. see server log for more information";
		
		if (e==null){
			return errorMessage;
		}
		
		Throwable t = e;
		
		while (t != null) {
			errorMessage = t.getLocalizedMessage();
			t=t.getCause();
		}
		
		return errorMessage;
	}

}