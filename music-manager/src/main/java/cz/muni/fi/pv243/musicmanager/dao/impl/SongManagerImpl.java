package cz.muni.fi.pv243.musicmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.lucene.search.Query;
import org.infinispan.Cache;
import org.infinispan.commons.CacheException;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.infinispan.query.dsl.QueryFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.pv243.musicmanager.dao.SongManager;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.utils.UUIDStringGenerator;

/**
 * Infinispan cache access for {@link Song} entity.
 * @author filip
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SongManagerImpl implements SongManager {
	
	public static final String SONG_CACHE_NAME = "songcache";
	private static final Logger logger = LoggerFactory.getLogger(SongManagerImpl.class);
	
	@Inject
	private CacheContainerProvider provider;
	
	@Inject
    private UserTransaction userTransaction;
    
	private BasicCache<String, Song> songCache;
	
	
	@Override
	public void createSong(Song song) throws IllegalEntityException, IllegalArgumentException, CacheException {
		if (song == null) {
			throw new IllegalArgumentException("Song is null.");
		}
		if (song.getId() != null) {
			throw new IllegalEntityException("Song id is already assigned (not null).");
		}
		
		song.setId(UUIDStringGenerator.generateSongId());
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		try {
			userTransaction.begin();
			songCache.put(song.getId(), song);
			userTransaction.commit();
			logger.info("Song \""+song.getSongName()+"\" was put to the cache.");
		} catch (Exception e) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception rbEx) {
					logger.error("Transaction rollback error.", rbEx);
				}
			}
			logger.error("Error while trying to put song with name: " + song.getSongName() + " to the cache.", e);
			throw new CacheException(e); 
		}
	}

	@Override
	public void updateSong(Song song) throws NonExistingEntityException, IllegalArgumentException {
		if(song == null){
			throw new IllegalArgumentException("Song is null.");
		}
		if(song.getId() == null){
			throw new IllegalArgumentException("Song id is null.");
		}
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		if(!songCache.containsKey(song.getId())){
			throw new NonExistingEntityException("Song does not exist in cache.");
		}
		
		try {
			userTransaction.begin();
			songCache.put(song.getId(), song);
			userTransaction.commit();
			logger.info("Song with id: " + song.getId() + " was updated in cache store.");
		} catch (Exception e) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception ex) {
					logger.error("Transaction rollback error.", ex);
				}
			}
			logger.error("Error while updating song.", e);
			throw new CacheException(e); 
		}
	}
	
	@Override
	public void removeSong(Song song) throws NonExistingEntityException, IllegalArgumentException, CacheException {
		if (song == null) {
			throw new IllegalArgumentException("Song is null.");
		}
		if (song.getId() == null) {
			throw new IllegalArgumentException("Song id is null.");
		}
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		if(!songCache.containsKey(song.getId())){
			throw new NonExistingEntityException("Song does not exist in cache.");
		}
		
		try {
			userTransaction.begin();
            songCache.remove(song.getId());
            userTransaction.commit();
            logger.info("Song with id: " + song.getId() + " was removed from the cache.");
        } catch (Exception e) {
            if (userTransaction != null) {
                try {
                	userTransaction.rollback();
                } catch (Exception ex) {
                	logger.error("Transaction rollback error.", ex);
                }
            }
            logger.error("Error while trying to remove with id: " + song.getId() + " from the cache.", e);
            throw new CacheException(e);
        }
	}

	@Override
	public Song getSong(String id) throws IllegalArgumentException, CacheException {
		if (id == null) {
			throw new IllegalArgumentException("Id is null.");
		}
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		if(!songCache.containsKey(id)){
			return null;
		}
		
		try {
			userTransaction.begin();
            Song song = songCache.get(id);
            userTransaction.commit();
            return song;
        } catch (Exception e) {
            if (userTransaction != null) {
                try {
                	userTransaction.rollback();
                } catch (Exception ex) {
                	logger.error("Transaction rollback error.", ex);
                }
            }
            logger.error("Error while trying to retrieve song with id: " + id + " from the cache.", e);
            throw new CacheException(e);
        }
	}
	
	@Override
	public List<Song> getAllSongs() throws CacheException {
		if(songCache.isEmpty()) {
			return null;
		}
		
		List<Song> songs = new ArrayList<Song>();
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		SearchManager sm = Search.getSearchManager((Cache<String, Song>) songCache);
		
		Query q = sm.buildQueryBuilderForClass(Song.class).get()
				.all().createQuery();

		logger.debug("Lucene query: " + q);
		
		CacheQuery cq = sm.getQuery(q, Song.class);
				
		for (Object o : cq.list()) {
			if (o instanceof Song) {
				songs.add(((Song) o));
				}
			}
		
		return songs;
	}

	@Override
	public List<Song> getTop10Songs() throws CacheException {
		if(songCache.isEmpty()) {
			return null;
		}
		
		List<Song> songs = new ArrayList<Song>();
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		SearchManager sm = Search.getSearchManager((Cache<String, Song>) songCache);
		
		Query q = sm.buildQueryBuilderForClass(Song.class).get().all().createQuery();
				//.keyword().onField("timesPlayed").matching("0L").createQuery();
		logger.debug("Lucene query: " + q);
		
		CacheQuery cq = sm.getQuery(q, Song.class);
				
		for (Object o : cq.list()) {
			if (o instanceof Song) {
				songs.add(((Song) o));
				}
			}
		
		return songs;
	}

	@Override
	public List<Song> getSongsbyInterpret(String interpretId) throws IllegalArgumentException {
		if (interpretId == null) {
			throw new IllegalArgumentException("Interpret id is null.");
		}
		
		List<Song> songs = new ArrayList<Song>();
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		SearchManager sm = Search.getSearchManager((Cache<String, Song>) songCache);
		
		Query q = sm.buildQueryBuilderForClass(Song.class).get()
				.keyword().onField("interpretId").matching(interpretId).createQuery();
		logger.debug("Lucene query: " + q);
		
		CacheQuery cq = sm.getQuery(q, Song.class);
		
		for (Object o : cq.list()) {
			if (o instanceof Song) {
				songs.add(((Song) o));
				}
			}
		
		return songs;
	}

	@Override
	public List<Song> searchSongs(String fulltext) throws IllegalArgumentException {
		if (fulltext == null || fulltext.length() < 1) {
			throw new IllegalArgumentException("Search string is null or empty.");
		}
		
		List<Song> songs = new ArrayList<Song>();
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		SearchManager sm = Search.getSearchManager((Cache<String, Song>) songCache);
		
		QueryFactory qf = sm.getQueryFactory();
		
		Query q = sm.buildQueryBuilderForClass(Song.class).get()
				.keyword().onField("songName").matching(fulltext).createQuery(); // keyword search

		logger.debug("Lucene query: " + q);
		
		CacheQuery cq = sm.getQuery(q, Song.class);
		
		for (Object o : cq.list()) {
			if (o instanceof Song) {
				songs.add(((Song) o));
				}
			}
		
		return songs;
	}
	
	@Override
	public List<Song> getUserSongs(String userName) throws IllegalArgumentException {
		if (userName == null) {
			throw new IllegalArgumentException("UserName id is null.");
		}
		
		List<Song> songs = new ArrayList<Song>();
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		SearchManager sm = Search.getSearchManager((Cache<String, Song>) songCache);
		
		Query q = sm.buildQueryBuilderForClass(Song.class).get()
				.keyword().onField("uploaderUserName").matching(userName).createQuery();
		logger.debug("Lucene query: " + q);
		
		CacheQuery cq = sm.getQuery(q, Song.class);
		
		for (Object o : cq.list()) {
			if (o instanceof Song) {
				songs.add(((Song) o));
				}
			}
		
		return songs;
	}
	
	@Override
	public void removeAllSongs() {
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		try {
			userTransaction.begin();
			songCache.clear();
			userTransaction.commit();
		} catch (Exception e) {
            if (userTransaction != null) {
                try {
                	userTransaction.rollback();
                } catch (Exception ex) {
                	logger.error("Transaction rollback error.", ex);
                }
            }
            logger.error("Error while trying to clear song cache.", e);
            throw new CacheException(e);
        }
		
	}
	
}
