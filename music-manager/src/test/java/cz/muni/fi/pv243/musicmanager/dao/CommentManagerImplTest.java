package cz.muni.fi.pv243.musicmanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJBException;
import javax.inject.Inject;

import org.infinispan.commons.CacheException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.dao.CommentManager;
import cz.muni.fi.pv243.musicmanager.dao.SongManager;
import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.utils.UUIDStringGenerator;

/**
 * Tests for the CommentManagerImpl class.
 * @author Radek Koubsky
 */
@RunWith(Arquillian.class)
public class CommentManagerImplTest {
	@Deployment
    public static WebArchive createDeployment() {
		return ShrinkWrap.create(MavenImporter.class, "music-manager-test.war")
				  .loadPomFromFile("pom.xml", "arq-jbossas-managed")
				  .importBuildOutput().as(WebArchive.class);
    }
    
    @Inject
	private CommentManager commentManager;
    
    @Inject
    SongManager songManager;
    
    @Test
    @InSequence(1)
    public void testCreateNullComment() throws IllegalEntityException {
    	try {
    		commentManager.createComment(null);
    		Assert.fail("IllegalArgumentException not thrown.");
    	} catch (EJBException ex) {
    		if(!(ex.getCausedByException() instanceof IllegalArgumentException)){
    			Assert.fail("Wrong type of exception.");
    		}
    	} catch(Exception ex){
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
    @InSequence(2)
    public void testCreateCommentWithId() {
    	Comment comment = newComment(UUIDStringGenerator.generateCommentId(), "hello", "Kouba", "2");
    	try {
			commentManager.createComment(comment);
			Assert.fail("IllegalEntityException not thrown.");
    	} catch (IllegalEntityException ex) {
    		//ok
    	} catch(Exception ex){
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
    @InSequence(3)
    public void testCreateAndGetCorrectComment() throws IllegalArgumentException, IllegalEntityException, NonExistingEntityException{
    	Comment actual = newComment(null, "I am comment.", "Kouba", "1");
    	
    	commentManager.createComment(actual);
    	
    	Comment expected = commentManager.getCommentById(actual.getId());
    	assertEquals(expected, actual);
    	commentManager.deleteComment(actual);
    }
    
    @Test
    @InSequence(4)
    public void testUpdateNullComment(){
    	try {
    		commentManager.updateComment(null);
    		Assert.fail("IllegalArgumentException not thrown.");
    	} catch (EJBException ex) {
    		if(!(ex.getCausedByException() instanceof IllegalArgumentException)){
    			Assert.fail("Wrong type of exception thrown.");
    		}
    	} catch(Exception ex){
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
    @InSequence(5)
    public void testUpdateCommentWithNullId(){
    	Comment comment = newComment(null, "hello", "Kouba", "2");
    	try {
    		commentManager.updateComment(comment);
    		Assert.fail("IllegalArgumentException not thrown.");
    	} catch (EJBException ex) {
    		if(!(ex.getCausedByException() instanceof IllegalArgumentException)){
    			Assert.fail("Wrong type of exception thrown.");
    		}
    	} catch(Exception ex){
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
    @InSequence(6)
    public void testUpdateNonExsitingComment(){
    	Comment notInStoreComment = newComment(UUIDStringGenerator.generateCommentId(), "I am not stored.", "kouba", "1");
    	try {
			commentManager.updateComment(notInStoreComment);
			Assert.fail("NonExistingEntityException not thrown");
		} catch (NonExistingEntityException e) {
			//ok
		}
    }
    
    @Test
    @InSequence(7)
    public void testUpdateCorrectComment() throws IllegalArgumentException, IllegalEntityException, NonExistingEntityException{
    	Comment actual = newComment(null, "I am not stored.", "kouba", "1");
    	
    	commentManager.createComment(actual);
    	assertNotNull(commentManager.getCommentById(actual.getId()));
    	actual.setText("My text was set.");
    	commentManager.updateComment(actual);
    	Comment expected = commentManager.getCommentById(actual.getId());
    	assertNotNull(expected);
    	assertEquals(expected, actual);
    	commentManager.deleteComment(expected);
    }
    
    @Test
    @InSequence(8)
    public void testDeleteNullComment(){
    	try {
    		commentManager.deleteComment(null);
    		Assert.fail("IllegalArgumentException not thrown.");
    	} catch (EJBException ex) {
    		if(!(ex.getCausedByException() instanceof IllegalArgumentException)){
    			Assert.fail("Wrong type of exception thrown.");
    		}
    	} catch(Exception ex){
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
    @InSequence(10)
    public void testDeleteCommentWithNullId(){
    	Comment comment = newComment(null, "hello", "Kouba", "2");
    	try {
    		commentManager.deleteComment(comment);
    		Assert.fail("IllegalArgumentException not thrown.");
    	} catch (EJBException ex) {
    		if(!(ex.getCausedByException() instanceof IllegalArgumentException)){
    			Assert.fail("Wrong type of exception thrown.");
    		}
    	} catch(Exception ex){
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
    @InSequence(11)
    public void testDeleteNonExistingComment(){
    	Comment notInStoreComment = newComment(UUIDStringGenerator.generateCommentId(), "I am not stored.", "kouba", "1");
    	try {
			commentManager.deleteComment(notInStoreComment);
			Assert.fail("NonExistingEntityException not thrown");
		} catch (NonExistingEntityException e) {
			//ok
		}
    }
    
    @Test
    @InSequence(12)
    public void testDeleteComment() throws IllegalArgumentException, IllegalEntityException, NonExistingEntityException{
    	Comment comment = newComment(null, "hello", "Kouba", "2");
    	
    	commentManager.createComment(comment);
    	assertNotNull(commentManager.getCommentById(comment.getId()));
    	commentManager.deleteComment(comment);
    	assertNull(commentManager.getCommentById(comment.getId()));
    }
    
    @Test
    @InSequence(13)
    public void testGetCommentsByNullSongId(){
    	try {
    		commentManager.getCommentsBySongId(null);
    		Assert.fail("IllegalArgumentException not thrown.");
    	} catch (EJBException ex) {
    		if(!(ex.getCausedByException() instanceof IllegalArgumentException)){
    			Assert.fail("Wrong type of exception thrown.");
    		}
    	} catch(Exception ex){
    		Assert.fail("Wrong type of exception thrown.");
    	}
    }
    
    @Test
    @InSequence(14)
    public void testGetCommentsBySongId() throws CacheException, IllegalEntityException, NonExistingEntityException{
    	Song song = new Song();
    	song.setFilePath("/asd.file");
    	song.setInterpretId("Noisia");
    	song.setSongName("Could this be");
    	song.setTimesPlayed(0);
    	song.setUploaderUserName("kouba");
    	
    	songManager.createSong(song);
    	
    	Comment comment1 = newComment(null, "I am comment 1", "kouba", song.getId());
    	commentManager.createComment(comment1);
    	Comment comment2 = newComment(null, "I am comment 2", "kouba", song.getId());
    	commentManager.createComment(comment2);
    	Comment comment3 = newComment(null, "I am comment 3", "kouba", song.getId());
    	commentManager.createComment(comment3);
    	ArrayList<Comment> comments = new ArrayList<Comment>();
    	comments.add(comment1);
    	comments.add(comment2);
    	comments.add(comment3);
    	song.setComments(comments);
    	
    	songManager.updateSong(song);
    	List<Comment> result = commentManager.getCommentsBySongId(song.getId());
    	Collections.sort(comments, idComparator);
    	Collections.sort(result, idComparator);
    	
    	for (int i = 0; i < result.size(); i++) {
			assertEquals(result.get(i), comments.get(i));
		}
    	
    	commentManager.deleteComment(comment1);
    	commentManager.deleteComment(comment2);
    	commentManager.deleteComment(comment3);
    	songManager.removeSong(song);
    }
    
    private Comment newComment(String id, String text, String authorUserName, String songId){
    	Comment comment = new Comment();
    	comment.setId(id);
    	comment.setAuthorUserName(authorUserName);
    	comment.setSongId(songId);
    	comment.setText(text);
    	comment.setPostTime(new Date());
    	return comment;
    }
    
    private static Comparator<Comment> idComparator = new Comparator<Comment>() {

        @Override
        public int compare(Comment c1, Comment c2) {
            return UUID.fromString(c1.getId()).compareTo(UUID.fromString(c2.getId()));
        }
    };

}
