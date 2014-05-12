package cz.muni.fi.pv243.backend.dao;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.dao.CommentManager;
import cz.muni.fi.pv243.musicmanager.dao.impl.CacheContainerProvider;
import cz.muni.fi.pv243.musicmanager.dao.impl.CommentManagerImpl;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;

/**
 * Tests for the CommentManagerImpl class.
 * @author Radek Koubsky
 */
@RunWith(Arquillian.class)
public class CommentManagerImplTest {
	@Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "music-manager.war")
        		.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        		.addClasses(CommentManager.class, CacheContainerProvider.class, CommentManagerImpl.class, Logger.class);
    }
    
    @Inject
	CommentManager commentManager;
    
    @Test
    public void testNullComment() throws IllegalEntityException {
    	try {
    		commentManager.createComment(null);
    		Assert.fail("IllegalArgumentException not thrown.");
    	} catch (IllegalArgumentException ex) {
    		
    	}
        
    }

}
