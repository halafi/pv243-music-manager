package cz.muni.fi.pv243.musicmanager.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import cz.muni.fi.pv243.musicmanager.dao.UserManager;
import cz.muni.fi.pv243.musicmanager.entities.User;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;

@Stateless
public class UserServiceImpl implements UserService{
	
	@Inject
	private UserManager userManager;
	
	@Inject
	private Logger logger;
	
	@Inject
	private Validator validator;
	
	@Override
	public void createUser(User user) throws ServiceException {
		try{
			validateUser(user);
			userManager.createUser(user);
		}catch(Exception ex){
			logger.debug("User user is not valid",ex);
			throw new ServiceException ("User user is not valid",ex);
		}
	}

	@Override
	public void removeUser(User user) throws ServiceException {
		try{
			validateUser(user);
			userManager.removeUser(user);
		}catch(Exception ex){
			logger.debug("User user is not valid",ex);
			throw new ServiceException ("User user is not valid",ex);
		}
	}

	@Override
	public void updateUser(User user) throws ServiceException {
		try{
			validateUser(user);
			userManager.updateUser(user);
		}catch(Exception ex){
			logger.debug("User"+user.getUsername()+" is not valid",ex);
			throw new ServiceException ("User user is not valid",ex);
		}
	}

	@Override
	public User getUserByUsername(String username) throws ServiceException {
		User user = null;
		try{
			user = userManager.getUserByUsername(username);
		}catch(Exception ex){
			logger.debug("Could not retrieve user"+user.getUsername(),ex);
			throw new ServiceException ("Could not retrieve user" +user.getUsername(),ex);
		}
		return user;
	}

	@Override
	public List<User> getAllUsers() throws ServiceException {
		List<User> users = null;
		try{
			users=userManager.getAllUsers();
		}catch(Exception ex){
			logger.debug("Could not retrieve all users");
			throw new ServiceException ("Could not retrieve all users");
		}
		return users;
	}

	@Override
	public void removeAllUsers() throws ServiceException {
		try{
			userManager.removeAllUsers();
		}catch(Exception ex) {
			logger.debug("Failed to remove all users.", ex);
			throw new ServiceException("Failed to remove all users.", ex);
		}
	}
	
	private void validateUser(User user) throws ConstraintViolationException {
		if(user == null){
			throw new IllegalArgumentException("User is null.");
		}
		
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		
		logger.info("Validation completed. violations found: " + violations.size());
	
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                new HashSet<ConstraintViolation<?>>(violations));
        }
	}
}
