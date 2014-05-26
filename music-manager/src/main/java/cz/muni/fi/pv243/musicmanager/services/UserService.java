package cz.muni.fi.pv243.musicmanager.services;

import java.util.List;

import cz.muni.fi.pv243.musicmanager.entities.User;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
/**
 * For javadoc to these methods look into interface UserManager.
 * @author Daniel
 *
 */
public interface UserService {
	
	public void createUser(User user) throws ServiceException;
	
	public void removeUser(User user) throws ServiceException;
	
	public void updateUser(User user) throws ServiceException;
	
	public User getUserByUsername(String username) throws ServiceException;
	
	public List<User> getAllUsers() throws ServiceException;
	
	public void removeAllUsers() throws ServiceException;
	
}
