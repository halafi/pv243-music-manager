package cz.muni.fi.pv243.backend.dao;

//import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.backend.dao.impl.SongManagerImpl;
import cz.muni.fi.pv243.backend.entities.Song;
import cz.muni.fi.pv243.exceptions.EntityExistsException;

// http://arquillian.org/guides/getting_started/?utm_source=cta
@RunWith(Arquillian.class)
public class SongManagerImplTest {
	
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(SongManagerImpl.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    //@Inject
    SongManagerImpl songManager;

    @Test
    public void CreateNullSong() throws EntityExistsException {
    	try {
    		songManager.createSong(null);
    	} catch (IllegalArgumentException ex) {
    		
    	}
        Assert.fail("IllegalArgumentException not thrown.");
    }
}