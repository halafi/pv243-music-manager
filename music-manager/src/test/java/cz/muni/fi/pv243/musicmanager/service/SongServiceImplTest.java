package cz.muni.fi.pv243.musicmanager.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.SongService;

/**
 * Tests for the SongManagerImpl class. Tests are not independent.
 * @author filip
 */
@RunWith(Arquillian.class)
public class SongServiceImplTest {

	@Deployment
    public static WebArchive createDeployment() {
		return ShrinkWrap.create(MavenImporter.class, "test-comment-service.war")
				  .loadPomFromFile("pom.xml", "arq-jbossas-managed")
				  .importBuildOutput().as(WebArchive.class);
    }
	
	@Inject
	private SongService songService;
	
    @Before
    public void setUp() throws ServiceException {
    	songService.removeAllSongs();
    }
    
    @After
    public void tearDown() throws ServiceException {
    	songService.removeAllSongs();
    }
    
    @Test
    @InSequence(1)
    public void testCRUD() {
    	List<Comment> comments = new ArrayList<Comment>();
    	Song expected = newSong(null, "Various Artists", "Homer", "The Simpsons Theme", "/simpson-theme.mp3", comments, 0L);
    	try {
    		// test create
			songService.createSong(expected);
			assertNotNull(expected.getId());
			Song actual = songService.getSong(expected.getId());
			assertDeepEquals(actual, expected);
			// test update
	    	expected.setSongName("Star Wars Theme");
	    	expected.setInterpretId("John Williams");
	    	songService.updateSong(expected);
	    	actual = songService.getSong(expected.getId());
	    	assertDeepEquals(actual, expected);
	    	// test delete
	    	songService.removeSong(actual);
	    	assertNull(songService.getSong(actual.getId()));
	    	songService.removeSong(actual);
		} catch (ServiceException ex) { // fail on remove non existing entity
			if(!(ex.getCause() instanceof NonExistingEntityException)) {
				Assert.fail("Wrong Exception thrown.");
			}
		} catch (Exception ex) {
			Assert.fail("Should be ok.");
		}
    }
    
	@Test
	@InSequence(2)
    public void testSongValidator() {
		List<Comment> comments = new ArrayList<Comment>();
		Comment c0valid = newComment(null, "I am comment.", "Filipi", "1");
		Comment c1invalid = newComment(null, "    ", "Filipi", "1");
		Song succeed0 = newSong(null, "Various Artists", "Homer", "The Simpsons Theme", "/simpson-theme.mp3", comments, 0L);
		Song failToValidate0 = newSong(null, null, "Homer", "The Simpsons Theme", "/simpson-theme.mp3", comments, 0L);
		Song failToValidate1 = newSong(null, "", "Homer", "The Simpsons Theme", "/simpson-theme.mp3", comments, 0L);
		Song failToValidate2 = newSong(null, "Various Artists", "  ", "The Simpsons Theme", "/simpson-theme.mp3", comments, 0L);
		Song failToValidate3 = newSong(null, "Various Artists", "Homer", "The Simpsons Theme", "/simpson-theme.mp3", null, 0L);
		Song failToValidate4 = newSong(null, "Various Artists", "Homer", "The Simpsons Theme", "/simpson-theme.mp3", comments, -1L);
		Song succeed1 = newSong(null, "Various Artists", "Homer", "The Simpson Theme", "/simpson-theme.mp3", comments, 0L);
		Song failToValidateOnComment = newSong(null, "Various Artists", "Homer", "The Simpson Theme", "/simpson-theme.mp3", comments, 0L);
		try {
			songService.createSong(succeed0);
		} catch (Exception ex) {
			Assert.fail("Should be ok.");
		}
		try {
			songService.createSong(failToValidate0);
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		try {
			songService.createSong(failToValidate1);
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		try {
			songService.createSong(failToValidate2);
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("  ", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		try {
			songService.createSong(failToValidate3);
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		try {
			songService.createSong(failToValidate4);
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(-1L, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		comments.add(c0valid);
		try {
			songService.createSong(succeed1);
		} catch (Exception ex) {
			Assert.fail("Should be ok.");
		}
		comments.add(c1invalid);
		try {
			songService.createSong(failToValidateOnComment);
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("    ", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
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
     * Simple {@link Comment} constructor.
     */
    private Comment newComment(String id, String text, String authorUserName, String songId){
    	Comment comment = new Comment();
    	comment.setId(id);
    	comment.setAuthorUserName(authorUserName);
    	comment.setSongId(songId);
    	comment.setText(text);
    	comment.setPostTime(new Date());
    	return comment;
    }
}
