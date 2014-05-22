package cz.muni.fi.pv243.musicmanager.dao;

import java.util.List;

import org.infinispan.commons.CacheException;

import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;

/**
 * @author Roman Macor
 * */

public interface InterpretManager {

	/**
	 * Puts new {@link Interpret} to the cache store.
	 * @param interpret new interpret to be added
	 * @throws IllegalArgumentException when interpret is null.
	 * @throws IllegalEntityException if interpret id is already set.
	 * */
	public void createInterpret(Interpret interpret) throws IllegalEntityException, IllegalArgumentException;
	
	/**
	 * Retrieves {@link Interpret} by id.
	 * @param id id of the interpret to be retrieved
	 * @return Interpret instance with specified id or null when Interpret doesn't exists
	 * @throws IllegalArgumentException when interpret id is null.
	 * */
	public Interpret getInterpretById(String id);
	
	/**
	 * Updates existing {@link Interpret} in cache store.
	 * @param interpret interpret to be updated
	 * @throws IllegalArgumentException when interpret is null.
	 * @throws NonExistingEntityException when Interpret doesn't exist in cache store.
	 * */
	public void updateInterpret(Interpret interpret) throws NonExistingEntityException, IllegalArgumentException;
	
	/**
	 * Remove {@link Interpret} from cache store.
	 * @param interpret interpret to be removed
	 * @throws IllegalArgumentException when interpret is null.
	 * @throws NonExistingEntityException when Interpret doesn't exist in cache store.
	 * */
	public void deleteInterpret(Interpret interpret) throws NonExistingEntityException, IllegalArgumentException;

	/**
	 * Retrieves every {@link Interpret} from the cache.
	 * @return list of all interprets
	 * @throws CacheException on infinispan cache operation error
	 */
	public List<Interpret> getAllInterprets() throws CacheException;
	
	
	/**
	 * Retrieves a list of each {@link Interpret} containing the search string in the interpret name.
	 * @param fulltext string to be searched for as a factor of interpret name
	 * @return list of all interprets with the factor fulltext in the interpret name
	 * @throws IllegalArgumentException if fulltext is null or empty
	 */
	public List<Interpret> searchInterprets(String fulltext) throws IllegalArgumentException;
	
	/**
	 * Removes all {@link Interpret} entities from the cache.
	 */
	public void removeAllInterprets();
}
