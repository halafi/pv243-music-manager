package cz.muni.fi.pv243.musicmanager.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Set;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.CommentService;

@RunWith(Arquillian.class)
public class CommentServiceImplTest {
	@Deployment
    public static WebArchive createDeployment() {
		return ShrinkWrap.create(MavenImporter.class, "test-comment-service.war")
				  .loadPomFromFile("pom.xml", "arq-jbossas-managed")
				  .importBuildOutput().as(WebArchive.class);
    }
	
	@Inject
	private CommentService commentService;
	
	@Test
	@InSequence(1)
    public void testCreateCommentWithIdNotNull() {
		Comment comment = newComment("2", "hello world", "Kouba", "5", new Date());
    
		try {
			commentService.createComment(comment);
			Assert.fail("IllegalEntityException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof IllegalEntityException){
				//ok
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(2)
    public void testCreateCommentWithNullAuthor() {
		Comment comment = newComment(null, "hello world", null, "5", new Date());
    
		try {
			commentService.createComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(3)
    public void testCreateCommentWithNullSongId() {
		Comment comment = newComment(null, "hello world", "Kouba", null, new Date());
    
		try {
			commentService.createComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(4)
    public void testCreateCommentWithNullPostTime() {
		Comment comment = newComment(null, "hello world", "Kouba", "5", null);
    
		try {
			commentService.createComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(5)
    public void testCreateCommentWithBlankText() {
		Comment comment = newComment(null, "", "Kouba", "5", new Date());
    
		try {
			commentService.createComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(2, violations.size());
				assertEquals("", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(6)
    public void testCreateCommentWithOnlyWhiteSpacesText() {
		Comment comment = newComment(null, "    ", "Kouba", "5", new Date());
    
		try {
			commentService.createComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("    ", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(7)
    public void testCreateCommentWithTooWideText() {
		Comment comment = newComment(null, "dfdfjdsakdjklsadjklsdjksdjksajdksajdksadjsajdsafjsdkgjdkgbjdfkghsdjfsdfsdfsfsdfjshjfshfkshfjshfjkshfksdhfjsdfdsfisdjfksdjfsklfsdlkfhdjghjfdkgbdfsklgbkjfbsdkjgbfdkjgbfdkgdslkfaklthdsgfndsajgnfdkjgkdfgbghjfsdfsdhfjkshfjksdhfkjdfhjsfhjsfhjkshfsdhfjshfjsdhfjsdhfjshfsfsfsdjfsdhf", "Kouba", "5", new Date());
    
		try {
			commentService.createComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("dfdfjdsakdjklsadjklsdjksdjksajdksajdksadjsajdsafjsdkgjdkgbjdfkghsdjfsdfsdfsfsdfjshjfshfkshfjshfjkshfksdhfjsdfdsfisdjfksdjfsklfsdlkfhdjghjfdkgbdfsklgbkjfbsdkjgbfdkjgbfdkgdslkfaklthdsgfndsajgnfdkjgkdfgbghjfsdfsdhfjkshfjksdhfkjdfhjsfhjsfhjkshfsdhfjshfjsdhfjsdhfjshfsfsfsdjfsdhf", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(8)
    public void testCreateNullComment() {
		try {
			commentService.createComment(null);
			Assert.fail("IllegalArgumentException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof IllegalArgumentException){
				//ok
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(9)
    public void testUpdateCommentWithNullAuthor() {
		Comment comment = newComment("1", "hello world", null, "5", new Date());
    
		try {
			commentService.updateComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(10)
    public void testUpdateCommentWithNullSongId() {
		Comment comment = newComment("1", "hello world", "Kouba", null, new Date());
    
		try {
			commentService.updateComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(12)
    public void testUpdateCommentWithNullPostTime() {
		Comment comment = newComment("1", "hello world", "Kouba", "5", null);
    
		try {
			commentService.updateComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(13)
    public void testUpdateCommentWithBlankText() {
		Comment comment = newComment("1", "", "Kouba", "5", new Date());
    
		try {
			commentService.updateComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(2, violations.size());
				assertEquals("", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(14)
    public void testUpdateCommentWithOnlyWhiteSpacesText() {
		Comment comment = newComment("1", "    ", "Kouba", "5", new Date());
    
		try {
			commentService.updateComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("    ", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(15)
    public void testUpdateCommentWithTooWideText() {
		Comment comment = newComment("1", "dfdfjdsakdjklsadjklsdjksdjksajdksajdksadjsajdsafjsdkgjdkgbjdfkghsdjfsdfsdfsfsdfjshjfshfkshfjshfjkshfksdhfjsdfdsfisdjfksdjfsklfsdlkfhdjghjfdkgbdfsklgbkjfbsdkjgbfdkjgbfdkgdslkfaklthdsgfndsajgnfdkjgkdfgbghjfsdfsdhfjkshfjksdhfkjdfhjsfhjsfhjkshfsdhfjshfjsdhfjsdhfjshfsfsfsdjfsdhf", "Kouba", "5", new Date());
    
		try {
			commentService.updateComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("dfdfjdsakdjklsadjklsdjksdjksajdksajdksadjsajdsafjsdkgjdkgbjdfkghsdjfsdfsdfsfsdfjshjfshfkshfjshfjkshfksdhfjsdfdsfisdjfksdjfsklfsdlkfhdjghjfdkgbdfsklgbkjfbsdkjgbfdkjgbfdkgdslkfaklthdsgfndsajgnfdkjgkdfgbghjfsdfsdhfjkshfjksdhfkjdfhjsfhjsfhjkshfsdhfjshfjsdhfjsdhfjshfsfsfsdjfsdhf", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(16)
    public void testUpdateNullComment() {
		try {
			commentService.updateComment(null);
			Assert.fail("IllegalArgumentException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof IllegalArgumentException){
				//ok
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(17)
    public void testDeleteNullComment() {
		try {
			commentService.deleteComment(null);
			Assert.fail("IllegalArgumentException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof EJBException){
				//ok
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(18)
    public void testCreateCommentWithNullText() {
		Comment comment = newComment(null, null, "Kouba", "5", new Date());
    
		try {
			commentService.createComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	@Test
	@InSequence(19)
    public void testUpdateCommentWithNullText() {
		Comment comment = newComment(null, null, "Kouba", "5", new Date());
    
		try {
			commentService.updateComment(comment);
			Assert.fail("ConstraintViolationException not thrown");
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException cx = (ConstraintViolationException)e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
    }
	
	private Comment newComment(String id, String text, String authorUserName, String songId, Date postTime){
    	Comment comment = new Comment();
    	comment.setId(id);
    	comment.setAuthorUserName(authorUserName);
    	comment.setSongId(songId);
    	comment.setText(text);
    	comment.setPostTime(postTime);
    	return comment;
    }
	
}
