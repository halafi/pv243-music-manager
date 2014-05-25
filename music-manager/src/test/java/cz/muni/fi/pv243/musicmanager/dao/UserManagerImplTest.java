package cz.muni.fi.pv243.musicmanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.validation.constraints.AssertTrue;

import org.fest.assertions.AssertExtension;
import org.infinispan.commons.api.BasicCacheContainer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.musicmanager.dao.UserManager;
import cz.muni.fi.pv243.musicmanager.dao.impl.CacheContainerProvider;
import cz.muni.fi.pv243.musicmanager.dao.impl.UserManagerImpl;
import cz.muni.fi.pv243.musicmanager.entities.User;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;

@RunWith(Arquillian.class)
public class UserManagerImplTest {

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(MavenImporter.class, "music-manager-test.war")
				.loadPomFromFile("pom.xml", "arq-jbossas-managed")
				.importBuildOutput().as(WebArchive.class);
	}

	@Inject
	private UserManager userManager;

	@Before
	public void setUp() {
		userManager.removeAllUsers();
	}

	@After
	public void tearDown() {
		userManager.removeAllUsers();
	}

	
	/*
	 * CreateUser tests.
	 */
	@Test
	@InSequence(1)
	public void testCreateNullUser() {
		try {
			userManager.createUser(null);
			Assert.fail("Should throw IllegalArgumentException.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}

	@Test
	@InSequence(2)
	public void testCreateExistentUser() {
		User user1 = newUser("test1crt@test.com", "Firsttest1crt", "Lasttest1crt",
				"test1crtPass", "Test1crt");
		try {
			userManager.createUser(user1);
		} catch (Exception ex) {
			Assert.fail("Failed to put user into cache.");
		}
		User user2 = newUser("test2crt@test.com", "Firsttest2crt", "Lasttest2crt",
				"test2crtPass", "Test1crt");
		try {
			userManager.createUser(user2);
			Assert.fail("Should throw IllegalEntityException.");
		} catch (IllegalEntityException ex) {
			// OK
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}

	}

	@Test
	@InSequence(3)
	public void testCreateAndGetUserByUsername() {
		User actual = newUser("actualcrt@actualcrt.com", "Firstactualcrt", "Lastactualcrt",
				"actualcrtPass", "Actualcrt");
		try {
			userManager.createUser(actual);
		} catch (Exception ex) {
			Assert.fail("Failed to put user into cache.");
		}
		User expected = userManager.getUserByUsername("Actualcrt");
		assertEquals(expected, actual);
	}

	
	/*
	 * UpdateUser tests.
	 */
	@Test
	@InSequence(4)
	public void testUpdateNullUser() {
		try {
			userManager.updateUser(null);
			Assert.fail("Should throw IllegalArgumentException.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}
	
	
	
	@Test
	@InSequence(5)
	public void testUpdateAndGetUserByUsername() {
		User actual = newUser("actualupd@actualcrt.com", "Firstactualupd", "Lastactualupd",
				"actualupdPass", "Actualupd");
		try {
			userManager.createUser(actual);
		} catch (Exception ex) {
			Assert.fail("Failed to put user into cache.");
		}
		try {
			actual.setEmail("actualupd2@actualupd2.com");
			actual.setFirstname("Firstactual2upd");
			actual.setLastname("Lastactualupd2"); 
			actual.setPassword("actualupd2Pass");
			userManager.updateUser(actual);
		} catch (Exception ex) {
			Assert.fail("Failed to put user into cache.");
		}
		User expected = userManager.getUserByUsername("Actualupd");
		assertEquals(expected, actual);
	}
	
	
	
	@Test
	@InSequence(6)
	public void testUpdateUsernameWithNull() {
		User actual = newUser("actualNullupd@actualcrt.com", "FirstactualNullupd", "LastactualNullupd",
				"actualupdNullPass", null);
		
		try {
			userManager.updateUser(actual);
			Assert.fail("Should throw IllegalArgumentException.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}
	
	@Test
	@InSequence(7)
	public void testUpdateNonExistentUser(){
		User notExistUser = newUser("not@exist.email","notExistFst","notExistLst", "nonExistentPass","notExistUser");
		try{
			userManager.updateUser(notExistUser);
			Assert.fail("Should throw NonExistingEntityException");
		}catch(NonExistingEntityException ex){
			//OK
		}
	}
	
	
	/*
	 * Remove user test
	 */
	@Test
	@InSequence(8)
	public void testRemoveNullUser() {
		try {
			userManager.removeUser(null);
			Assert.fail("Should throw IllegalArgumentException.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}
	
	@Test
	@InSequence(9)
	public void testRemoveNonExistentUser(){
		User notExistUser = newUser("remnot@exist.email","remnotExistFst","remnotExistLst", "remnonExistentPass","remnotExistUser");
		try{
			userManager.removeUser(notExistUser);
			Assert.fail("Should tthrow NonExistingEntityException");
		}catch(NonExistingEntityException ex){
			//OK
		}
	}
	
	@Test
	@InSequence(10)
	public void testRemoveNullUsername(){
		User nullExistUser = newUser("remnull@exist.email","remnullExistFst","remnullExistLst", "remnullExistentPass",null);
		try{
			userManager.removeUser(nullExistUser);
			Assert.fail("Should tthrow IllegalArgumentException");
		}catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	
	}
	
	@Test
	@InSequence(11)
	public void testRemoveUser() throws IllegalArgumentException, IllegalEntityException, NonExistingEntityException{
		User user = newUser("user@user.com", "userFst", "userLst", "userPass", "userName");
		userManager.createUser(user);
		assertNotNull(userManager.getUserByUsername("userName"));
		userManager.removeUser(user);
		assertNull(userManager.getUserByUsername("userName"));
	}
	
	
	/*
	 *GetUserByUsername test 
	 */
	@Test
	@InSequence(12)
	public void testGetCorrectUser() throws IllegalArgumentException, IllegalEntityException{
		User user = newUser("getUser@email.com","getUserFst", "getUserLst","getUserPass","getUsername");
		userManager.createUser(user);
		User expected=userManager.getUserByUsername("getUsername");
		assertEquals(expected, user);
	}
	
	
	/*
	 * GetAllUsers test 
	 */
	@Test
	@InSequence(13)
	public void testGetAllUsers() throws IllegalArgumentException, IllegalEntityException{
		userManager.removeAllUsers();
		
		User testUser1 = newUser("user1@test.com","testUser1Fst", "testUser1Lst", "testUser1Pass", "testUsername1");
		User testUser2 = newUser("user2@test.com","testUser2Fst", "testUser2Lst", "testUser2Pass", "testUsername2");
		User testUser3 = newUser("user3@test.com","testUser3Fst", "testUser3Lst", "testUser3Pass", "testUsername3");
		userManager.createUser(testUser1);
		userManager.createUser(testUser2);
		userManager.createUser(testUser3);
		
		List<User>expected = new ArrayList<User>();
		expected.add(testUser3);
		expected.add(testUser2);
		expected.add(testUser1);
		assertTrue(userManager.getAllUsers().containsAll(expected));
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
