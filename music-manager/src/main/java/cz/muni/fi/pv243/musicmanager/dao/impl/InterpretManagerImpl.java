package cz.muni.fi.pv243.musicmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.infinispan.Cache;
import org.infinispan.commons.CacheException;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.slf4j.Logger;
import cz.muni.fi.pv243.musicmanager.dao.InterpretManager;
import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.utils.UUIDStringGenerator;

/**
 * @author Roman Macor
 * */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class InterpretManagerImpl implements InterpretManager {

	@Inject
	private Logger logger;

	@Inject
	private CacheContainerProvider provider;

	@Inject
	private UserTransaction utx;

	private BasicCache<String, Interpret> interpretCache;

	private final String INTERPRET_CACHE_NAME = "interpretcache";

	@Override
	public void createInterper(Interpret interpret)
			throws IllegalEntityException, IllegalArgumentException {
		if (interpret == null) {
			throw new IllegalArgumentException("Interpret is null.");
		}

		if (interpret.getId() != null) {
			throw new IllegalEntityException(
					"Interpert id is not null, Interpret entity cannot be put into cache.");
		}
		interpretCache = provider.getCacheContainer().getCache(
				INTERPRET_CACHE_NAME);

		interpret.setId(UUIDStringGenerator.generateIntepretId());

		try {
			utx.begin();
			interpretCache.put(interpret.getId(), interpret);
			utx.commit();

			logger.info("Interpret with id: " + interpret.getId()
					+ " was inserted to cache store.");
		} catch (Exception e) {
			if (utx != null) {
				try {
					utx.rollback();
				} catch (Exception e1) {
					logger.error("Error while creating interpret.", e);
					throw new CacheException(e);

				}
			}
		}
	}

	@Override
	public Interpret getInterpretById(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Interpret id is null.");
		}
		interpretCache = provider.getCacheContainer().getCache(
				INTERPRET_CACHE_NAME);
		if (interpretCache.containsKey(id)) {
			return (Interpret) interpretCache.get(id);
		} else {
			return null;
		}
	}

	@Override
	public void updateInterpret(Interpret interpret)
			throws NonExistingEntityException, IllegalArgumentException {
		if (interpret == null) {
			throw new IllegalArgumentException("interpret is null.");
		}

		if (interpret.getId() == null) {
			throw new IllegalArgumentException("interpret id is null.");
		}

		interpretCache = provider.getCacheContainer().getCache(
				INTERPRET_CACHE_NAME);

		if (!interpretCache.containsKey(interpret.getId())) {
			throw new NonExistingEntityException(
					"interpret does not exist in cache.");
		}

		try {
			utx.begin();
			interpretCache.put(interpret.getId(), interpret);
			utx.commit();
		} catch (Exception e) {
			if (utx != null) {
				try {
					utx.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			logger.error("Error while updating interpret.", e);
			throw new CacheException(e);
		}

	}

	@Override
	public void deleteInterpret(Interpret interpret)
			throws NonExistingEntityException, IllegalArgumentException {
		if (interpret == null) {
			throw new IllegalArgumentException("interpret is null.");
		}

		if (interpret.getId() == null) {
			throw new IllegalArgumentException("interpret id is null.");
		}

		interpretCache = provider.getCacheContainer().getCache(
				INTERPRET_CACHE_NAME);

		if (!interpretCache.containsKey(interpret.getId())) {
			throw new NonExistingEntityException(
					"interpret does not exist in cache.");
		}

		try {
			utx.begin();
			interpretCache.remove(interpret.getId());
			utx.commit();
		} catch (Exception e) {
			if (utx != null) {
				try {
					utx.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			logger.error("Error while deleting interpret.", e);
			throw new CacheException(e);
		}

	}

	@Override
	public List<Interpret> getAllInterprets() throws CacheException {
		// List<Interpret> interprets = new ArrayList<Interpret>();
		List<Interpret> interprets = new ArrayList<Interpret>(
				interpretCache.values());

		/*
		 * for (Object o : interpretCache.values()) { Interpret i = (Interpret)
		 * o; interprets.add(i); }
		 */
		return interprets;
	}

	@Override
	public List<Interpret> searchInterprets(String fulltext)
			throws IllegalArgumentException {
		if (fulltext == null || fulltext.length() < 1) {
			throw new IllegalArgumentException(
					"Search string is null or empty.");
		}

		SearchManager sm = Search
				.getSearchManager((Cache<String, Interpret>) interpretCache);

		org.infinispan.query.dsl.Query q = sm.getQueryFactory()
				.from(Song.class).having("name").like("%" + fulltext + "%")
				.toBuilder().build();

		return q.list();
	}

	 @Override
	public void removeAllInterprets() {

		try {
			utx.begin();
			interpretCache.clear();
			utx.commit();
		} catch (Exception e) {
			if (utx != null) {
				try {
					utx.rollback();
				} catch (Exception ex) {
					logger.error("Transaction rollback error.", ex);
				}
			}
			logger.error("Error while trying to clear interpret cache.", e);
			throw new CacheException(e);
		}
	}
}
