package cz.muni.fi.pv243.musicmanager.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.dao.InterpretManager;
import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.InterpretService;

/**
 * Service layer for {@link Interpret} entity manipulation using InterpretManager.
 * @author Roman Macor
 */

public class InterpretServiceImpl implements InterpretService {

	@Inject
	private InterpretManager interpretManager;

	@Inject
	private Validator validator;

	@Inject
	private Logger logger;

	@Override
	public void createInterpret(Interpret interpret) throws ServiceException {

		try {
			validateInterpret(interpret);
			interpretManager.createInterpret(interpret);
		} catch (Exception e) {
			logger.debug(
					"Some exception was thrown by InterpretManager or while validating interpret.",
					e);
			throw new ServiceException(e);
		}
	}

	@Override
	public Interpret getInterpretById(String id) throws ServiceException {
		Interpret interpret = null;
		try {
			interpret = interpretManager.getInterpretById(id);
		} catch (Exception e) {
			logger.debug("Some exception was thrown by InterpretManager", e);
			throw new ServiceException(e);
		}
		return interpret;
	}

	@Override
	public void updateInterpret(Interpret interpret) throws ServiceException {
		try {
			validateInterpret(interpret);
			interpretManager.updateInterpret(interpret);
		} catch (Exception e) {
			logger.debug(
					"Some exception was thrown by InterpretManager or while validating the interpret.",
					e);
			throw new ServiceException(e);
		}

	}

	@Override
	public void deleteInterpret(Interpret interpret) throws ServiceException {
		try {
			validateInterpret(interpret);
			interpretManager.deleteInterpret(interpret);
		} catch (Exception e) {
			logger.debug(
					"Some exception was thrown by InterpretManager or while validating the interpret.",
					e);
			throw new ServiceException(e);
		}

	}

	@Override
	public List<Interpret> getAllInterprets() throws ServiceException {
		List<Interpret> result = new ArrayList<Interpret>();
		try {
			result = interpretManager.getAllInterprets();
		} catch (Exception e) {
			logger.debug("Some exception was thrown by InterpretManager.", e);
			throw new ServiceException(e);
		}
		
		return Collections.unmodifiableList(result);
	}

	@Override
	public List<Interpret> searchInterprets(String fulltext) throws ServiceException{
	List<Interpret> interprets = new ArrayList<Interpret>();
	try {
		interprets = interpretManager.searchInterprets(fulltext);
	} catch (Exception e) {
		logger.debug("Failed to search interprets.", e);
		throw new ServiceException("Failed to search songs.", e);
	}
	return interprets;
	}

	@Override
	public void removeAllInterprets() throws ServiceException {
		try{
			interpretManager.removeAllInterprets();
		}catch (Exception e){
			logger.debug("Failed to remove all interprets.", e);
			throw new ServiceException("Failed to remove all songs.", e);

		}

	}

	/**
	 * Validates the given {@link Interpret}
	 * 
	 * @param comment
	 *            Interpret to be validated
	 * @throws ConstraintViolationException
	 *             If Bean Validation errors exist
	 * */
	private void validateInterpret(Interpret interpret)
			throws ConstraintViolationException {
		if (interpret == null) {
			throw new IllegalArgumentException("Interpret is null.");
		}

		Set<ConstraintViolation<Interpret>> violations = validator
				.validate(interpret);

		logger.info("Validation completed. violations found: "
				+ violations.size());

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
					new HashSet<ConstraintViolation<?>>(violations));
		}
	}
}
