package cz.muni.fi.pv243.musicmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.infinispan.commons.CacheException;
import org.infinispan.commons.api.BasicCache;
import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.dao.UserManager;
import cz.muni.fi.pv243.musicmanager.entities.User;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;

/**
 * @author Daniel
 *
 */
@Stateless
@javax.ejb.TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class UserManagerImpl implements UserManager{
	@Inject
	private  Logger logger;
	
	@Inject
	private CacheContainerProvider provider;
	
	@Inject
    private UserTransaction userTransaction;
    
	private BasicCache<String, Object> userCache;
	

	@Override
	public void createUser(User user) throws IllegalEntityException, IllegalEntityException {
		if(user==null){
			throw new IllegalArgumentException("User is null!");
		}
		
		userCache = provider.getCacheContainer().getCache("usercache");
		
		if(userCache.containsKey(user.getUsername())){
			throw new IllegalEntityException("User already exists!");
		}
		try {
			userTransaction.begin();
			userCache.put(user.getUsername(), user);
			userTransaction.commit();
			logger.info("User "+user.getUsername()+" was put to the cache.");
		} catch (Exception trEx) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception rbEx) {
					logger.error("Transaction rollback error.", rbEx);
				}
			}
			logger.error("Error while trying to put user "+user.getUsername()+"to the cache.", trEx);
			throw new CacheException(trEx); 
		}

	}


	@Override
	public void removeUser(User user) throws IllegalArgumentException, NonExistingEntityException{
		if(user==null){
			throw new IllegalArgumentException("User is null!");
		}
		if(user.getUsername()==null){
			throw new IllegalArgumentException("User's username is null!");
		}
		userCache = provider.getCacheContainer().getCache("usercache");
		
		if(!userCache.containsKey(user.getUsername())){
			throw new NonExistingEntityException("User does not exists!");
		}
		try {
			userTransaction.begin();
			userCache.remove(user.getUsername(), user);
			userTransaction.commit();
			logger.info("User "+user.getUsername()+" was removed from the cache.");
		} catch (Exception trEx) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception rbEx) {
					logger.error("Transaction rollback error.", rbEx);
				}
			}
			logger.error("Error while trying to remove user "+user.getUsername()+"to the cache.", trEx);
			throw new CacheException(trEx); 
		}
	}


	@Override
	public void updateUser(User user) throws IllegalArgumentException, NonExistingEntityException {
		if(user==null){
			throw new IllegalArgumentException("User is null!");
		}
		if(user.getUsername()==null){
			throw new IllegalArgumentException("User's username is null!");
		}
		userCache = provider.getCacheContainer().getCache("usercache");
		
		if(!userCache.containsKey(user.getUsername())){
			throw new NonExistingEntityException("User does not exists!");
		}
		try{
			userTransaction.begin();
			userCache.put(user.getUsername(), user);
			userTransaction.commit();
			logger.info("User "+user.getUsername()+" was updated back to the cache.");
		} catch (Exception trEx) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception rbEx) {
					logger.error("Transaction rollback error.", rbEx);
				}
			}
			logger.error("Error while trying to update user "+user.getUsername()+"to the cache.", trEx);
			throw new CacheException(trEx); 
		}
		
	}


	@Override
	public User getUserByUsername(String username) throws IllegalArgumentException{
		if(username==null){
			throw new IllegalArgumentException("Param. username is null");
		}
		userCache = provider.getCacheContainer().getCache("usercache");
		
		if(userCache.containsKey(username))
			return (User) userCache.get(username);
		else
			return null;
	}


	@Override
	public List<User> getAllUsers() {
		userCache = provider.getCacheContainer().getCache("usercache");
		List<User> users = new ArrayList<User>();
		for(String username : userCache.keySet()){
			users.add((User) userCache.get(username));
		}
		return users;
	}


	@Override
	public void removeAllUsers() {
		userCache = provider.getCacheContainer().getCache("usercache");
		try{
			userTransaction.begin();
			userCache.clear();
			userTransaction.commit();
		}catch  (Exception trEx) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception rbEx) {
					logger.error("Transaction rollback error.", rbEx);
				}
			}
			logger.error("Error while trying to remove all users from cache", trEx);
			throw new CacheException(trEx); 
		}
		
	}
}

