package cz.muni.fi.pv243.musicmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.InterpretService;

@RunWith(Arquillian.class)
public class InterpretServiceImplTest {
	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap
				.create(MavenImporter.class, "test-comment-service.war")
				.loadPomFromFile("pom.xml", "arq-jbossas-managed")
				.importBuildOutput().as(WebArchive.class);
	}

	@Inject
	private InterpretService interpretService;

	@Before
	public void setUp() throws ServiceException {
		interpretService.removeAllInterprets();
	}

	@After
	public void tearDown() throws ServiceException {
		interpretService.removeAllInterprets();
	}
    @Test
    @InSequence(1)
    public void testCRUD() {
    	Interpret expected = new Interpret(null, "Pink", "USA", Interpret.Genre.POP);
    	
    	try {
    		// test create
    		interpretService.createInterpret(expected);
			
			assertNotNull(expected.getId());
			Interpret actual = interpretService.getInterpretById(expected.getId());
			
			assertEquals(actual, expected);
			// test update
	    	
	    	actual.setName("Avril");
	    	actual.setCountry("Canada");
	    	actual.setGenre(Interpret.Genre.PUNK);
	    	
	    	interpretService.updateInterpret(actual);
	    	
	    	expected = interpretService.getInterpretById(actual.getId());
	    	assertEquals(actual, expected);

	    	// test delete
	    	interpretService.deleteInterpret(actual);
	    	
	    	assertNull(interpretService.getInterpretById(actual.getId()));
	    	
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
    public void testInterpretValidator() {
		
		Interpret valid = new Interpret(null, "Avril", "USA", Interpret.Genre.POP);
		Interpret valid2 = new Interpret(null, "Avril", "USA", Interpret.Genre.POP);
		Interpret valid3 = new Interpret(null, "Avril", "USA", null);
		Interpret valid4 = new Interpret(null, "Avril", null, Interpret.Genre.POP);
		
		Interpret invalid2 = new Interpret(null, "", "USA", Interpret.Genre.POP);
		Interpret invalid3 = new Interpret(null, "   ", "USA", Interpret.Genre.POP);
				
		try {
			interpretService.createInterpret(valid);
			interpretService.createInterpret(valid2);
			interpretService.createInterpret(valid3);
			interpretService.createInterpret(valid4);
			
		} catch (Exception ex) {
			Assert.fail("Should be ok.");
		}
		try {
			interpretService.createInterpret(invalid2);
			
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
			interpretService.createInterpret(invalid3);
		} catch (ServiceException e) {
			if(e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cx = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> violations = cx.getConstraintViolations();
				assertEquals(1, violations.size());
				assertEquals("   ", violations.iterator().next().getInvalidValue());
			} else {
				Assert.fail("Wrong type of Exception was thrown");
			}
		}
		
		
	}
}
