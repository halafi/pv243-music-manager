package cz.muni.fi.pv243.backend.dao;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.dao.impl.SongManagerImpl;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;

/**
 * Tests for the SongManagerImpl class.
 * @author filip
 */
/*@RunWith(Arquillian.class)
public class SongManagerImplTest {
	

    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                //.addPackages(true, SongManagerImpl.class.getPackage()).addClass(SongManagerImpl.class)
                .addClass(SongManagerImpl.class);
    }
    
    @Inject
    SongManagerImpl songManager;

    @Test
    public void createNullSong() throws IllegalEntityException {
    	
    	try {
    		songManager.createSong(null);
    	} catch (IllegalArgumentException ex) {
    		
    	}
        Assert.fail("IllegalArgumentException not thrown.");
    }
}*/
