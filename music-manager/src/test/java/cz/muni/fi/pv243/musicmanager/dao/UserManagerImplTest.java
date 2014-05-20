package cz.muni.fi.pv243.musicmanager.dao;

import javax.inject.Inject;
import javax.transaction.UserTransaction;


import org.infinispan.commons.api.BasicCacheContainer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.dao.UserManager;
import cz.muni.fi.pv243.musicmanager.dao.impl.CacheContainerProvider;
import cz.muni.fi.pv243.musicmanager.dao.impl.UserManagerImpl;
import cz.muni.fi.pv243.musicmanager.entities.User;
@RunWith(Arquillian.class)
public class UserManagerImplTest {
  
   
	
	@Deployment
	public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(CacheContainerProvider.class)
            .addClass(BasicCacheContainer.class)
            .addClass(UserTransaction.class)
            .addClass(UserManagerImpl.class) 
            .addAsWebResource(EmptyAsset.INSTANCE, "beans.xml");
    }

	@Inject 
    UserManager userManager;
    
    @Test
    public void testCreateUser() {
    	System.out.println("Creating new User");
    	User user = new User();
    	user.setEmail("test@test.com");
    	user.setFirstname("Daniel");
    	user.setLastname("Sak");
    	user.setPassword("secretPass");
    	user.setUsername("dsDandzi");
    	
    	userManager.createUser(user);
        System.out.println("-----ns-----");
    }
}
