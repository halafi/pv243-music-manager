package cz.muni.fi.pv243.musicmanager.services;

import java.util.List;

import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;

/**
 * Service layer for {@link Interpret} entity manipulation using InterpretManager.
 * @author Roman Macor
 */

public interface InterpretService {

	public void createInterpret(Interpret interpret) throws ServiceException;
	
	public Interpret getInterpretById(String id) throws ServiceException;
	
	public void updateInterpret(Interpret interpret) throws ServiceException;
	
	public void deleteInterpret(Interpret interpret) throws ServiceException;

	public List<Interpret> getAllInterprets() throws ServiceException;
	
	public List<Interpret> searchInterprets(String fulltext) throws ServiceException;
	
	public void removeAllInterprets() throws ServiceException;
	
	
}
