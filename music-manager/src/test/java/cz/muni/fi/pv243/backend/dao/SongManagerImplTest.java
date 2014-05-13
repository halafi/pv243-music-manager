package cz.muni.fi.pv243.backend.dao;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.infinispan.commons.CacheException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.dao.SongManager;
import cz.muni.fi.pv243.musicmanager.dao.impl.CacheContainerProvider;
import cz.muni.fi.pv243.musicmanager.dao.impl.SongManagerImpl;
import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;

/**
 * Tests for the SongManagerImpl class.
 * @author filip
 */
@RunWith(Arquillian.class)
public class SongManagerImplTest {
	
    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(SongManagerImpl.class, SongManager.class) // Song Manager
                .addClass(CacheContainerProvider.class) // Infinispan configuration
                .addClass(Song.class) // Entities
                .addPackage("cz.muni.fi.pv243.musicmanager.exceptions")
                .addPackage("org.infinispan.commons")
                .addPackage("org.infinispan.commons.api");
    }
    
    @Inject
    SongManagerImpl songManager;
    
    
    @Test
    @InSequence(1)
    public void createSong_onNull() throws CacheException, IllegalEntityException {
    	try {
    		songManager.createSong(null);
    	} catch (IllegalArgumentException ex) {
    	}
    	fail();
    }
    
    @Test
    @InSequence(2)
    public void createSong() {
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"neexistujicisoubor", null, 0);
    	try {
			songManager.createSong(simpSong);
		} catch (IllegalArgumentException | CacheException | IllegalEntityException e) {
			fail();
		}
    	assertNotNull(simpSong.getId());
    	Song simpSongClone = songManager.getSong(simpSong.getId());
    	assertEquals(simpSong, simpSongClone);
    	assertNotSame(simpSong, simpSongClone);
    	assertDeepEquals(simpSong, simpSongClone);
    }
    
    @Test
    @InSequence(3)
    public void removeSong_onNull() throws CacheException, NonExistingEntityException {
    	try {
			songManager.removeSong(null);
		} catch (IllegalArgumentException e) {
			
		}
    	fail();
    }
    
    @Test
	@InSequence(4)
    public void removeSong() {
    	Song simpSong = newSong(null, "Various Artists", "Homer", "The Simpsons Theme",
    			"/home/filip/neexistujicisoubor", null, 0);
    	try {
			songManager.createSong(simpSong);
		} catch (IllegalArgumentException | CacheException | IllegalEntityException e) {
			fail();
		}
    	try {
			songManager.removeSong(simpSong);
		} catch (IllegalArgumentException | CacheException | NonExistingEntityException e) {
			fail();;
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
    	 assertDeepEquals(s1.getComments(), s2.getComments()); // TBD deep equals
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
