package cz.muni.fi.pv243.musicmanager.dao;

import java.util.List;


import cz.muni.fi.pv243.musicmanager.entities.User;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;


/**
 * @author Daniel
 *
 */
public interface UserManager {


	/**
	 * Creates new {@link User}.
	 * @param User which will be created, users unique value is username.
	 * @throws IllegalArgumentException if user is null.
	 * @thorws IllegalEntityException if user already exists
	 **/
	public void createUser(User user) throws IllegalArgumentException, IllegalEntityException;
	
	
	/**
	 * Removes given {@link User}.
	 * @param user which will be deleted
	 * @throws IllegalArgumentException if user is null.
	 * @throws NonExistingEntityException if user does not exist. 
	 **/
	public void removeUser(User user) throws IllegalArgumentException, NonExistingEntityException;
	
	
	/**
	 * Updates given {@link User}.
	 * @param user which will be updated
	 * @throws IllegalArgumentException if user is null.
	 * @throws NonExistingEntityException if user does not exist. 
	 **/
	public void updateUser(User user) throws IllegalArgumentException, NonExistingEntityException;
	

	
	/**
	 * @param username, unique value of each user.
	 * @throws IllegalArgumentException if user is null.
	 * @return User by username.
	 **/
	public User getUserByUsername(String username) throws IllegalArgumentException;
	
	
	/**
	 * 
	 * @return List of all existing users.
	 */
	public  List<User> getAllUsers();
	
	
	/**
	 * Removes all {@link User} from cache
	 **/
	public void removeAllUsers();
}


