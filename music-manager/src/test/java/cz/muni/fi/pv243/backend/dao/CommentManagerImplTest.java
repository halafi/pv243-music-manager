package cz.muni.fi.pv243.backend.dao;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.dao.impl.CommentManagerImpl;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;

/**
 * Tests for the CommentManagerImpl class.
 * @author Radek Koubsky
 */
@RunWith(Arquillian.class)
public class CommentManagerImplTest {
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "commentManagerImplTest.jar")
            .addClass(CommentManagerImpl.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Inject
	CommentManagerImpl commentManager;
    
    @Test
    public void testNullComment() throws IllegalEntityException {
    	try {
    		commentManager.createComment(null);
    	} catch (IllegalArgumentException ex) {
    		
    	}
        Assert.fail("IllegalArgumentException not thrown.");
    }

}
