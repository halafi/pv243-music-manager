package cz.muni.fi.pv243.backend.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.dao.SongManager;
import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.utils.UUIDStringGenerator;

/**
 * Tests for the SongManagerImpl class.
 * @author filip
 */
@RunWith(Arquillian.class)
public class SongManagerImplTest {
	
    
    @Deployment
    public static WebArchive getDeployment() {
    	return ShrinkWrap.create(MavenImporter.class, "music-manager-test.war")
				  .loadPomFromFile("pom.xml", "arq-jbossas-managed")
				  .importBuildOutput().as(WebArchive.class);
    }
    
    @Inject
    SongManager songManager;
    
    @Before
    public void setUpTest() {
    	songManager.removeAllSongs();
    }
    
    @After
    public void clean() {
    	songManager.removeAllSongs();
    }
    
    @Test
    @InSequence(1)
    public void createSongTest() {
    	// test create null
    	try {
    		songManager.createSong(null);
    		Assert.fail("Exception not thrown.");
    	} catch (EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
    		Assert.fail("Wrong type of exception thrown.");
    	}
    	// test create Song with id
    	Song simpSong = newSong(UUIDStringGenerator.generateSongId(), "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.createSong(simpSong);
    		Assert.fail("IllegalEntityException not thrown.");
    	} catch (IllegalEntityException ex) {
    		// OK
    	} catch (Exception ex) {
    		Assert.fail("IllegalEntityException not thrown.");
    	}
    	// testSong create and get
    	simpSong.setId(null);
    	try {
			songManager.createSong(simpSong);
		} catch (Exception ex) {
			Assert.fail("Failed to put song into cache.");
		}
    	assertNotNull(simpSong.getId());
    	Song simpSongClone = songManager.getSong(simpSong.getId());
    	assertEquals(simpSong, simpSongClone);
    	assertSame(simpSong, simpSongClone);
    	assertDeepEquals(simpSong, simpSongClone);
    }
    
    @Test
	@InSequence(2)
    public void updateSongTest() {
    	// test update null
    	try {
    		songManager.updateSong(null);
    		Assert.fail("Exception not thrown.");
    	} catch (EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
    		Assert.fail("Wrong type of exception thrown.");
    	}
    	// test update non-existing with null id
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.updateSong(simpSong);
    		Assert.fail("Exception not thrown.");
    	} catch (EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
    		Assert.fail("Wrong type of exception thrown.");
    	}
    	// test update non-existing
    	simpSong.setId(UUIDStringGenerator.generateSongId());
    	try {
    		songManager.updateSong(simpSong);
    		Assert.fail("Wrong type of exception thrown.");
    	} catch (NonExistingEntityException ex) {
    		// OK
    	} catch (Exception ex) {
    		Assert.fail("Wrong type of exception thrown.");
    	}
    	// test create and update
    	Song actual = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.createSong(actual);
    	} catch (Exception ex) {
    		Assert.fail("Failed to put song into cache.");
    	}
    	actual.setTimesPlayed(1L);
    	try {
    		songManager.updateSong(actual);
    	} catch (Exception ex) {
    		Assert.fail("Failed to update song in cache.");
    	}
    	Song expected = songManager.getSong(actual.getId());
    	assertNotNull(expected);
    	assertEquals(expected, actual);
    	if (expected.getTimesPlayed() != 1L) {
    		Assert.fail("Failed to update song in cache.");
    	}
    }
    
    @Test
	@InSequence(3)
    public void removeSongTest() {
    	// test remove null
    	try {
			songManager.removeSong(null);
		} catch (EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
    	// test remove Song with null id
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.removeSong(simpSong);
    	} catch (EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
    		Assert.fail("Wrong type of exception thrown.");
    	}
    	// test create and remove Song
    	try {
			songManager.createSong(simpSong);
		} catch (Exception ex) {
			Assert.fail("Failed to create song.");
		}
    	assertNotNull(songManager.getSong(simpSong.getId()));
    	try {
			songManager.removeSong(simpSong);
		} catch (Exception ex) {
			Assert.fail("Failed to remove song.");
		}
    	assertNull(songManager.getSong(simpSong.getId()));
    	// test remove non-existing (already removed) Song
    	try {
    		songManager.removeSong(simpSong);
    		Assert.fail("NonExistingEntityException not thrown.");
    	} catch (NonExistingEntityException ex) {
    		// OK
    	} catch (Exception ex) {
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
	@InSequence(4)
    public void getSongTest() {
    	
    }
    
    @Test
	@InSequence(5)
    public void getTop10SongsTest() {
    	
    }
    
    @Test
	@InSequence(6)
    public void getSongsbyInterpretTest() {
    	
    }
    
    @Test
	@InSequence(7)
    public void searchSongsTest() {
    	
    }
    
    @Test
	@InSequence(8)
    public void getUserSongsTest() {
    	
    }
    
    /**
     * Simple {@link Song} constructor.
     */
    public static Song newSong(String id, String interpretId, String uploaderIdUserName,
    		String songName, String filePath, List<Comment> comments, long timesPlayed) {
    	Song song = new Song();
    	song.setId(id);
    	song.setInterpretId(interpretId);
    	song.setUploaderUserName(uploaderIdUserName);
    	song.setSongName(songName);
    	song.setFilePath(filePath);
    	song.setComments(comments);
    	song.setTimesPlayed(timesPlayed);
    	return song;
    }
    
    /**
     * {@link Song} deep equals.
     */
    private void assertDeepEquals(Song s1, Song s2) {
    	 assertEquals(s1.getId(), s2.getId());
    	 assertEquals(s1.getFilePath(), s2.getFilePath());
    	 assertEquals(s1.getInterpretId(), s2.getInterpretId());
    	 assertEquals(s1.getSongName(), s2.getSongName());
    	 assertEquals(s1.getTimesPlayed(), s2.getTimesPlayed());
    	 assertEquals(s1.getUploaderUserName(), s2.getUploaderUserName());
    	 assertDeepEquals(s1.getComments(), s2.getComments());
    }
    
    /**
     * {@link Comment} list deep equals.
     */
    private void assertDeepEquals(List<Comment> l1, List<Comment> l2) {
        for (int i = 0; i < l1.size(); i++) {
            Comment expected = l1.get(i);
            Comment actual = l2.get(i);
            assertDeepEquals(expected, actual);
        }
    }
    
    /**
     * {@link Comment} deep equals.
     */
    private void assertDeepEquals(Comment c1, Comment c2) {
    	assertEquals(c1.getId(), c2.getId());
    	assertEquals(c1.getAuthorUserName(), c2.getAuthorUserName());
    	assertEquals(c1.getPostTime(), c2.getPostTime());
    	assertEquals(c1.getSongId(), c2.getSongId());
    	assertEquals(c1.getText(), c2.getText());
    	assertEquals(c1.getText(), c2.getText());
    }
    
    /**
     * {@link Song} comparator by id.
     */
    private static Comparator<Song> idComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return s1.getId().compareTo(s2.getId());
        }
    };
}
