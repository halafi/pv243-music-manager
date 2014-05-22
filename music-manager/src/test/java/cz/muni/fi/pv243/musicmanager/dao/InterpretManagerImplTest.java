package cz.muni.fi.pv243.musicmanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
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

import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.utils.UUIDStringGenerator;

/**
 * Tests for the CommentManagerImpl class.
 * 
 * @author Roman Macor
 */

@RunWith(Arquillian.class)
public class InterpretManagerImplTest {

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(MavenImporter.class, "music-manager-test.war")
				.loadPomFromFile("pom.xml", "arq-jbossas-managed")
				.importBuildOutput().as(WebArchive.class);
	}

	@Inject
	private InterpretManager interpretManager;

//All tests pass when this is uncommneted...
	@Before
	public void setUp() {
		interpretManager.removeAllInterprets();
	}

	@After
	public void tearDown() {
		interpretManager.removeAllInterprets();
	}

	@Test
	@InSequence(1)
	public void testCreateNullInterpret() throws IllegalEntityException {
		try {
			interpretManager.createInterpret(null);
			Assert.fail("IllegalArgumentException not thrown.");
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
	public void testCreateInterpretWithId() {
		Interpret interpret = new Interpret();
		interpret.setId("id");
		try {
			interpretManager.createInterpret(interpret);
			Assert.fail("IllegalEntityException not thrown.");
		} catch (IllegalEntityException ex) {
			// ok
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}

	@Test
	@InSequence(3)
	public void testCreateAndGetCorrectInterpret()
			throws IllegalArgumentException, IllegalEntityException,
			NonExistingEntityException {
		Interpret interpret = new Interpret(null, "Bon Jovi", "England",
				Interpret.Genre.ROCK);

		interpretManager.createInterpret(interpret);

		Interpret expected = interpretManager.getInterpretById(interpret
				.getId());

		assertEquals(expected, interpret);

		assertEquals(expected.getCountry(), interpret.getCountry());
		assertEquals(expected.getGenre(), interpret.getGenre());
		assertEquals(expected.getId(), interpret.getId());
		assertEquals(expected.getName(), interpret.getName());
		

		// Returning to previous state
		interpretManager.deleteInterpret(interpret);
	}

	@Test
	@InSequence(4)
	public void testUpdateNullInterpret() {
		try {
			interpretManager.updateInterpret(null);
			Assert.fail("IllegalArgumentException not thrown.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception thrown.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}

	@Test
	@InSequence(5)
	public void testUpdateInterpretWithNullId() {
		Interpret interpret = new Interpret(null, "Pink", "USA",
				Interpret.Genre.POP);
		try {
			interpretManager.updateInterpret(interpret);
			Assert.fail("IllegalArgumentException not thrown.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception thrown.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}

	@Test
	@InSequence(6)
	public void testUpdateNonExsitingInterpret() {
		Interpret notInStoreInterpret = new Interpret(
				UUIDStringGenerator.generateIntepretId(), "Pink", "USA",
				Interpret.Genre.POP);
		try {
			interpretManager.updateInterpret(notInStoreInterpret);
			Assert.fail("NonExistingEntityException not thrown");
		} catch (NonExistingEntityException e) {
			// ok
		}
	}

	@Test
	@InSequence(7)
	public void testUpdateCorrectInterpret() throws IllegalArgumentException,
			IllegalEntityException, NonExistingEntityException {
		Interpret actual = new Interpret(null, "Pink", "USA",
				Interpret.Genre.POP);

		interpretManager.createInterpret(actual);
		assertNotNull(interpretManager.getInterpretById(actual.getId()));
		actual.setGenre(Interpret.Genre.ROCK);
		interpretManager.updateInterpret(actual);

		Interpret expected = interpretManager.getInterpretById(actual.getId());
		assertNotNull(expected);
		assertEquals(expected, actual);
		interpretManager.deleteInterpret(actual);
	}

	@Test
	@InSequence(8)
	public void testDeleteNullInterpret() {
		try {
			interpretManager.deleteInterpret(null);
			Assert.fail("IllegalArgumentException not thrown.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception thrown.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}

	@Test
	@InSequence(10)
	public void testDeleteInterpretWithNullId() {

		Interpret interpret = new Interpret(null, "Pink", "USA",
				Interpret.Genre.POP);
		try {
			interpretManager.deleteInterpret(interpret);
			Assert.fail("IllegalArgumentException not thrown.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception thrown.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
	}

	@Test
	@InSequence(11)
	public void testDeleteNonExistingInterpret() {

		Interpret interpret = new Interpret(
				UUIDStringGenerator.generateIntepretId(), "Pink", "USA",
				Interpret.Genre.POP);
		try {
			interpretManager.deleteInterpret(interpret);
			Assert.fail("NonExistingEntityException not thrown");
		} catch (NonExistingEntityException e) {
			// ok
		}
	}

	@Test
	@InSequence(12)
	public void testDeleteComment() throws IllegalArgumentException,
			IllegalEntityException, NonExistingEntityException {
		Interpret interpret = new Interpret(null, "Pink", "USA",
				Interpret.Genre.POP);

		interpretManager.createInterpret(interpret);
		assertNotNull(interpretManager.getInterpretById(interpret.getId()));
		interpretManager.deleteInterpret(interpret);

		assertNull(interpretManager.getInterpretById(interpret.getId()));
	}

	@Test
	@InSequence(13)
	public void searchInterpretTest() {
		// search null fulltext
		try {
			//interpretManager.removeAllInterprets();
			@SuppressWarnings("unused")
			List<Interpret> interprets = interpretManager.searchInterprets(null);
			Assert.fail("Exception not thrown.");
		} catch (EJBException ex) {
			if (!(ex.getCausedByException() instanceof IllegalArgumentException)) {
				Assert.fail("Wrong type of exception thrown.");
			}
		} catch (Exception ex) {
			Assert.fail("Wrong type of exception thrown.");
		}
		// search empty cache
		try {
			List<Interpret> interprets = interpretManager
					.searchInterprets("Avril");
			if (!interprets.isEmpty()) {
				Assert.fail("Array is not empty.");
			}
		} catch (Exception ex) {
			Assert.fail("Exception thrown." + ex.getMessage());
		}
		// search for "Avril"

		Interpret interpret1 = new Interpret(null, "Avril Lavigne", "USA",
				Interpret.Genre.POP);
		Interpret interpret2 = new Interpret(null, "Pink", "USA",
				Interpret.Genre.POP);
		Interpret interpret3 = new Interpret(null, "Avril not Lovin", "USA",
				Interpret.Genre.POP);

		try {
			interpretManager.createInterpret(interpret1);
			interpretManager.createInterpret(interpret2);
			interpretManager.createInterpret(interpret3);

		} catch (Exception ex) {
			Assert.fail("Failed to create interpret.");
		}
		List<Interpret> expected = new ArrayList<Interpret>();
		expected.add(interpret1);
		expected.add(interpret2);
		try {
			List<Interpret> actual = interpretManager.searchInterprets("Avril");
			if (actual.size() != expected.size()) {
				Assert.fail("Array size does not match. Actual: "
						+ actual.size() + ", Expected: " + expected.size());
			}
			if (!actual.contains(interpret1) || !actual.contains(interpret3)) {
				Assert.fail("Does not contain proper interprets");
			}

		} catch (Exception ex) {
			Assert.fail("Failed to get interpret by name.");
		}

	}

	@Test
	@InSequence(14)
	public void getAllInterpretsTest() {
		
		// get interprets form an empty cache
		try {
			interpretManager.removeAllInterprets();
			List<Interpret> interprets = interpretManager.getAllInterprets();
			if (!interprets.isEmpty()) {
				Assert.fail("Array is not empty.");
			}
		} catch (Exception ex) {
			Assert.fail("Exception thrown.");
		}
		// create and get
		Interpret interpret1 = new Interpret(null, "Avril Lavigne", "USA",
				Interpret.Genre.POP);
		Interpret interpret2 = new Interpret(null, "Pink", "USA",
				Interpret.Genre.POP);
		Interpret interpret3 = new Interpret(null, "Avril not Lovin", "USA",
				Interpret.Genre.POP);
		try {
			interpretManager.createInterpret(interpret1);
			interpretManager.createInterpret(interpret2);
			interpretManager.createInterpret(interpret3);
		} catch (Exception ex) {
			Assert.fail("Failed to create interpret.");
		}
		List<Interpret> expected = new ArrayList<Interpret>();

		expected.add(interpret1);
		expected.add(interpret2);
		expected.add(interpret3);

		try {
			List<Interpret> actual = interpretManager.getAllInterprets();
			if (actual.size() != expected.size()) {
				Assert.fail("Array size does not match. actual size = " + actual.size());
			}
			//cleaning up
			interpretManager.removeAllInterprets();

		} catch (Exception ex) {
			Assert.fail("Failed to get all  interprets.");
		}
	}

}
