package cz.muni.fi.pv243.musicmanager.dao.impl;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.infinispan.commons.CacheException;
import org.infinispan.commons.api.BasicCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.pv243.musicmanager.dao.UserManager;
import cz.muni.fi.pv243.musicmanager.entities.User;

@Model
//@Stateless
public class UserManagerImpl implements UserManager{
	public static final String USER_CACHE_NAME = "usercache";
	private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
	
	@Inject
	private CacheContainerProvider provider;
	
	@Inject
    private UserTransaction userTransaction;
    
	private BasicCache<String, User> userCache;
	

	@Override
	public User createUser(User user) {
		userCache = provider.getCacheContainer().getCache(USER_CACHE_NAME);
		try {
			userTransaction.begin();
			userCache.put(user.getUsername(), user);
			userTransaction.commit();
			logger.info("User \""+user.getUsername()+"\" was put to the cache.");
		} catch (Exception trEx) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception rbEx) {
					logger.error("Transaction rollback error.", rbEx);
				}
			}
			logger.error("Error while trying to put user \""+user.getUsername()+"\" to the cache.", trEx);
			throw new CacheException(trEx); 
		}
		return null;
	}
}
