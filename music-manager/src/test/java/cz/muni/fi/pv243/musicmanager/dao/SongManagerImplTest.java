package cz.muni.fi.pv243.musicmanager.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
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
 * Tests for the SongManagerImpl class. Tests are not independent.
 * @author filip
 */
@RunWith(Arquillian.class)
public class SongManagerImplTest {
	
    @Deployment
    public static WebArchive getDeployment() {
    	return ShrinkWrap.create(MavenImporter.class, "test-song-manager.war")
				  .loadPomFromFile("pom.xml", "arq-jbossas-managed")
				  .importBuildOutput().as(WebArchive.class);
    }
    
    @Inject
    private SongManager songManager;
    
    @Before
    public void setUp() {
    	songManager.removeAllSongs();
    }
    
    @After
    public void tearDown() {
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
    		Assert.fail("Exception not thrown.");
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
			Assert.fail("Exception not thrown.");
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
    	// get with null id
    	try {
			Song song = songManager.getSong(null);
			Assert.fail("Exception not thrown.");
    	} catch (EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
    	// create and get
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	Song swSong = newSong(null, "Various Artists", "Yoda (Dark Side)", "Star Wars Theme",
    			"/star-wars-theme.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.createSong(simpSong);
    		songManager.createSong(swSong);
    	} catch (Exception ex) {
    		Assert.fail("Failed to create song.");
    	}
    	assertDeepEquals(songManager.getSong(simpSong.getId()), simpSong);
    	assertDeepEquals(songManager.getSong(swSong.getId()), swSong);
    }
    
    @Test
    @InSequence(5)
    public void getAllSongsTest() {
    	// get songs form an empty cache
    	try {
    		List<Song> songs = songManager.getAllSongs();
    		if (songs.size() != 0) {
    			Assert.fail("Array size is not 0.");
    		}
    	} catch (Exception ex) {
    		Assert.fail("Exception thrown.");
    	}
    	// create and get
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	Song swSong = newSong(null, "Various Artists", "Yoda (Dark Side)", "Star Wars Theme",
    			"/star-wars-theme.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.createSong(simpSong);
    		songManager.createSong(swSong);
    	} catch (Exception ex) {
    		Assert.fail("Failed to create song.");
    	}
    	List<Song> expected = new ArrayList<Song>();
    	expected.add(simpSong);
    	expected.add(swSong);
    	Collections.sort(expected, idComparator);
    	try {
    		List<Song> actual = songManager.getAllSongs();
    		if (actual.size() != 2) {
    			Assert.fail("Array size does not match.");
    		}
    		Collections.sort(actual, idComparator);
    		for (int i = 0; i < actual.size(); i++) {
    			assertEquals(actual.get(i), expected.get(i));
    		}
    	} catch (Exception ex) {
    		Assert.fail("Failed to get all (2) songs.");
    	}
    }
    
    @Test
    @InSequence(6)
    public void getTop10SongsTest() {
    	// get songs form an empty cache
    	try {
    		List<Song> songs = songManager.getTop10Songs();
    		if (songs.size() != 0) {
    			Assert.fail("Array size is not 0.");
    		}
    	} catch (Exception ex) {
    		Assert.fail("Exception thrown.");
    	}
    	// create and get songs
    	Song s0 = newSong(null, "Interpret", "User", "Song0", "/0.mp3",
    			new ArrayList<Comment>(), 2L);
    	Song s1 = newSong(null, "Interpret", "User", "Song1", "/1.mp3",
    			new ArrayList<Comment>(), 4L);
    	Song s2 = newSong(null, "Interpret", "User", "Song2", "/2.mp3",
    			new ArrayList<Comment>(), 8L);
    	Song s3 = newSong(null, "Interpret", "User", "Song3", "/3.mp3",
    			new ArrayList<Comment>(), 16L);
    	Song s4 = newSong(null, "Interpret", "User", "Song4", "/4.mp3",
    			new ArrayList<Comment>(), 32L);
    	Song s5 = newSong(null, "Interpret", "User", "Song5", "/5.mp3",
    			new ArrayList<Comment>(), 64L);
    	Song s6 = newSong(null, "Interpret", "User", "Song6", "/6.mp3",
    			new ArrayList<Comment>(), 128L);
    	Song s7 = newSong(null, "Interpret", "User", "Song7", "/7.mp3",
    			new ArrayList<Comment>(), 256L);
    	Song s8 = newSong(null, "Interpret", "User", "Song8", "/8.mp3",
    			new ArrayList<Comment>(), 512L);
    	Song s9 = newSong(null, "Interpret", "User", "Song9", "/9.mp3",
    			new ArrayList<Comment>(), 1024L);
    	Song s10 = newSong(null, "Interpret", "User", "Song10", "/10.mp3",
    			new ArrayList<Comment>(), 2048L);
    	try {
    		songManager.createSong(s0);
    		songManager.createSong(s1); songManager.createSong(s2);
    		songManager.createSong(s3); songManager.createSong(s4);
    		songManager.createSong(s5); songManager.createSong(s6);
    		songManager.createSong(s7); songManager.createSong(s8); 
    		songManager.createSong(s9); songManager.createSong(s10);
    	} catch (Exception ex) {
    		Assert.fail("Failed to create song.");
    	}
    	List<Song> expected = new ArrayList<Song>();
    	expected.add(s1); expected.add(s2); expected.add(s3); expected.add(s4);
    	expected.add(s5); expected.add(s6); expected.add(s7); expected.add(s8);
    	expected.add(s9); expected.add(s10);
    	Collections.sort(expected, timesPlayedComparator);
    	try {
    		List<Song> actual = songManager.getTop10Songs();
    		if (actual.size() != 10) {
    			Assert.fail("Array size does not match 10, is "+actual.size());
    		}
    		for (int i = 0; i < actual.size(); i++) {
    			assertEquals(actual.get(i), expected.get(i));
    		}
    	} catch (Exception ex) {
    		Assert.fail("Failed to get top 10 songs.");
    	}
    }
    
    @Test
    @InSequence(7)
    public void getSongsbyInterpretTest() {
    	// get songs by null interpret
    	try {
    		List<Song> songs = songManager.getSongsbyInterpret(null);
    		Assert.fail("Exception not thrown.");
    	} catch(EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
    	// get songs by interpret "Various Artists"
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	Song swSong = newSong(null, "Various Artists", "Yoda", "Star Wars Theme",
    			"/star-wars-theme.mp3", new ArrayList<Comment>(), 0);
    	Song inSong = newSong(null, "We don't want you here", "Homer", "Intruder",
    			"/my-precious.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.createSong(simpSong);
    		songManager.createSong(swSong);
    		songManager.createSong(inSong);
    	} catch (Exception ex) {
    		Assert.fail("Failed to create song.");
    	}
    	List<Song> expected = new ArrayList<Song>();
    	expected.add(simpSong);
    	expected.add(swSong);
    	Collections.sort(expected, idComparator);
    	try {
    		List<Song> actual = songManager.getSongsbyInterpret("Various Artists");
    		if (actual.size() != 3 && actual.size() != expected.size()) {
    			Assert.fail("Array size does not match.");
    		}
    		Collections.sort(actual, idComparator);
    		for (int i = 0; i < actual.size(); i++) {
    			assertEquals(actual.get(i), expected.get(i));
    		}
    	} catch (Exception ex) {
    		Assert.fail("Failed to get songs by interpret.");
    	}
    	// get songs by interpret "We don't want you here"
    	expected = new ArrayList<Song>();
    	expected.add(inSong);
    	try {
    		List<Song> actual = songManager.getSongsbyInterpret("We don't want you here");
    		if (actual.size() != 1 && actual.size() != expected.size()) {
    			Assert.fail("Array size does not match." + actual.size());
    		}
    		assertEquals(actual.get(0), expected.get(0));
    	} catch (Exception ex) {
    		Assert.fail("Failed to get songs by interpret.");
    	}
    }
    
    @Test
    @InSequence(8)
    public void searchSongsTest() {
    	// search null fulltext
    	try {
    		List<Song> songs = songManager.searchSongs(null);
    		Assert.fail("Exception not thrown.");
    	} catch(EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
    	// search empty cache
    	try {
    		List<Song> songs = songManager.searchSongs("Theme");
    		if (songs.size() != 0) {
    			Assert.fail("Array size is not 0.");
    		}
    	} catch (Exception ex) {
    		Assert.fail("Exception thrown.");
    	}
    	// search for "Theme"
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	Song swSong = newSong(null, "Various Artists", "Yoda", "Star Wars Theme",
    			"/star-wars-theme.mp3", new ArrayList<Comment>(), 0);
    	Song inSong = newSong(null, "We don't want you here", "Homer", "Intruder",
    			"/my-precious.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.createSong(simpSong);
    		songManager.createSong(swSong);
    		songManager.createSong(inSong);
    	} catch (Exception ex) {
    		Assert.fail("Failed to create song.");
    	}
    	List<Song> expected = new ArrayList<Song>();
    	expected.add(simpSong);
    	expected.add(swSong);
    	Collections.sort(expected, idComparator);
    	try {
    		List<Song> actual = songManager.searchSongs("Theme");
    		if (actual.size() != 2 || actual.size() != expected.size()) {
    			Assert.fail("Array size does not match. Actual: " + actual.size() +", Expected: " +expected.size());
    		}
    		Collections.sort(actual, idComparator);
    		for (int i = 0; i < actual.size(); i++) {
    			assertEquals(actual.get(i), expected.get(i));
    		}
    	} catch (Exception ex) {
    		Assert.fail("Failed to get songs by userName.");
    	}
    	
    }
    
    @Test
    @InSequence(9)
    public void getUserSongsTest() {
    	// get songs by null username
    	try {
    		List<Song> songs = songManager.getUserSongs(null);
    		Assert.fail("Exception not thrown.");
    	} catch(EJBException ex) {
    		if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
                Assert.fail("Wrong type of exception thrown.");
            }
    	} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
    	// get "Homer" songs
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/simpson-theme.mp3", new ArrayList<Comment>(), 0);
    	Song swSong = newSong(null, "Various Artists", "Yoda", "Star Wars Theme",
    			"/star-wars-theme.mp3", new ArrayList<Comment>(), 0);
    	Song inSong = newSong(null, "We don't want you here", "Homer", "Intruder",
    			"/my-precious.mp3", new ArrayList<Comment>(), 0);
    	try {
    		songManager.createSong(simpSong);
    		songManager.createSong(swSong);
    		songManager.createSong(inSong);
    	} catch (Exception ex) {
    		Assert.fail("Failed to create song.");
    	}
    	List<Song> expected = new ArrayList<Song>();
    	expected.add(simpSong);
    	expected.add(inSong);
    	Collections.sort(expected, idComparator);
    	try {
    		List<Song> actual = songManager.getUserSongs("Homer");
    		if (actual.size() != 2) {
    			Assert.fail("Array size does not match.");
    		}
    		Collections.sort(actual, idComparator);
    		for (int i = 0; i < actual.size(); i++) {
    			assertEquals(actual.get(i), expected.get(i));
    		}
    	} catch (Exception ex) {
    		Assert.fail("Failed to get songs by userName.");
    	}
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
    
    /**
     * {@link Song} comparator by timesPlayed.
     */
    private static Comparator<Song> timesPlayedComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return compare(s1.getTimesPlayed(), s2.getTimesPlayed());
        }
        
        public int compare(Long o1, Long o2) {
            return o1==null?Integer.MAX_VALUE:o2==null?Integer.MIN_VALUE:o2.compareTo(o1);
        }
    };
}
