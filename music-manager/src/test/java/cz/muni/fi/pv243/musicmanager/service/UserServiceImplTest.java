package cz.muni.fi.pv243.musicmanager.service;

import static org.junit.Assert.assertEquals;

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

import cz.muni.fi.pv243.musicmanager.entities.User;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.UserService;
/**
 * 
 * @author Daniel
 *
 */
@RunWith(Arquillian.class)
public class UserServiceImplTest {

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap
				.create(MavenImporter.class, "test-user-service.war")
				.loadPomFromFile("pom.xml", "arq-jbossas-managed")
				.importBuildOutput().as(WebArchive.class);
	}
	
	@Inject
	private UserService userService;
	
	@Before
	public void setUp() throws ServiceException{
		userService.removeAllUsers();
	}
	
	@After
	public void tearDown() throws ServiceException{
		userService.removeAllUsers();
	}
	
	/*
	 * Test for createUser
	 */
	@Test
	@InSequence(1)
	public void testCreateUserWithWrongParam(){
		
		User user = newUser("user@test.com", "userFst", "userSnd", "userPass", "username");
		
		try{
			userService.createUser(user);
		}catch (Exception ex) {
			Assert.fail("Failed to put user into cache.");
		}
		
		//test with null username
		try{
			user.setUsername(null);
			userService.createUser(user);
		} catch (ServiceException e){
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(2, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
				user.setUsername("username");
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		//test with empty string username
		try{
			user.setUsername("");
			userService.createUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(2, violations.size());
					assertEquals("", violations.iterator().next().getInvalidValue());
					user.setUsername("username");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		//test with empty string email
		try{
			user.setEmail("");
			userService.createUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("", violations.iterator().next().getInvalidValue());
					user.setEmail("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with null email
		try{
			user.setEmail(null);
			userService.createUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(2, violations.size());
					assertEquals(null, violations.iterator().next().getInvalidValue());
					user.setEmail("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with wrong format email
		try{
			user.setEmail("asdfsdgh");
			userService.createUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("asdfsdgh", violations.iterator().next().getInvalidValue());
					user.setEmail("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with too long string firstname
		try{
			user.setFirstname("abcdefghijabcdefghijabcdefghij");
			userService.createUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("abcdefghijabcdefghijabcdefghij", violations.iterator().next().getInvalidValue());
					user.setFirstname("userFst");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with too short string lastname
		try{
			user.setLastname("a");
			userService.createUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("a", violations.iterator().next().getInvalidValue());
					user.setLastname("userLst");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		
		
	}
	
	
	/*
	 * Test for updateUser
	 */
	@Test
	@InSequence(2)
	public void testUpdateUserWithWrongParam(){
		
		User user = newUser("user@test.com", "userFst", "userSnd", "userPass", "username");
		try{
			userService.createUser(user);
		}catch (Exception ex) {
			Assert.fail("Failed to put user into cache.");
		}
		
		//test with null username
		try{
			user.setUsername(null);
			userService.updateUser(user);
		} catch (ServiceException e){
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(2, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
				user.setUsername("username");
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		//test with empty string username
		try{
			user.setUsername("");
			userService.updateUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(2, violations.size());
					assertEquals("", violations.iterator().next().getInvalidValue());
					user.setUsername("username");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		//test with empty string email
		try{
			user.setEmail("");
			userService.updateUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("", violations.iterator().next().getInvalidValue());
					user.setUsername("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with null email
		try{
			user.setEmail(null);
			userService.updateUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(2, violations.size());
					assertEquals(null, violations.iterator().next().getInvalidValue());
					user.setEmail("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with wrong format email
		try{
			user.setEmail("asdfsdgh");
			userService.updateUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("asdfsdgh", violations.iterator().next().getInvalidValue());
					user.setEmail("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with too long string firstname
		try{
			user.setFirstname("abcdefghijabcdefghijabcdefghij");
			userService.updateUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("abcdefghijabcdefghijabcdefghij", violations.iterator().next().getInvalidValue());
					user.setFirstname("userFst");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with too short string lastname
		try{
			user.setLastname("a");
			userService.updateUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("a", violations.iterator().next().getInvalidValue());
					user.setLastname("userLst");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
	}
	
	
	
	/*
	 * Test for removeUser
	 */
	@Test
	@InSequence(3)
	public void testRemoveUserWithWrongParam(){
		
		User user = newUser("user@test.com", "userFst", "userSnd", "userPass", "username");
		try{
			userService.createUser(user);
		}catch (Exception ex) {
			Assert.fail("Failed to put user into cache.");
		}
		
		//test with null username
		try{
			user.setUsername(null);
			userService.removeUser(user);
		} catch (ServiceException e){
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(2, violations.size());
				assertEquals(null, violations.iterator().next().getInvalidValue());
				user.setUsername("username");
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		//test with empty string username
		try{
			user.setUsername("");
			userService.removeUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(2, violations.size());
					assertEquals("", violations.iterator().next().getInvalidValue());
					user.setUsername("username");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		//test with empty string email
		try{
			user.setEmail("");
			userService.removeUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("", violations.iterator().next().getInvalidValue());
					user.setUsername("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with null email
		try{
			user.setEmail(null);
			userService.removeUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(2, violations.size());
					assertEquals(null, violations.iterator().next().getInvalidValue());
					user.setEmail("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with wrong format email
		try{
			user.setEmail("asdfsdgh");
			userService.removeUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("asdfsdgh", violations.iterator().next().getInvalidValue());
					user.setEmail("user@test.com");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with too long string firstname
		try{
			user.setFirstname("abcdefghijabcdefghijabcdefghij");
			userService.removeUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("abcdefghijabcdefghijabcdefghij", violations.iterator().next().getInvalidValue());
					user.setFirstname("userFst");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}

		//test with too short string lastname
		try{
			user.setLastname("a");
			userService.removeUser(user);
		}
		catch (ServiceException e){
				if(e.getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
					Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
					assertEquals(1, violations.size());
					assertEquals("a", violations.iterator().next().getInvalidValue());
					user.setLastname("userLst");
				} else {
					Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
	}
	
	
	
	
	/*
	 * Creates New user
	 */
	public User newUser(String email, String firstname, String lastname,
			String password, String username) {
		User user = new User();
		user.setEmail(email);
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setPassword(password);
		user.setUsername(username);

		return user;
	}
	
}
