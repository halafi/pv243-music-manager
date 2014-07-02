package cz.muni.fi.pv243.musicmanager.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.slf4j.Logger;

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
	
	@Inject
	private Logger logger;
	
	@Inject
	private CacheContainerProvider provider;
	
	@Inject
    private UserTransaction userTransaction;
	
	public static final String SONG_CACHE_NAME = "songcache";
    
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
		List<Song> songs = new ArrayList<Song>();
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		//This doesn't seem to get data from cache store
		/*
		SearchManager sm = Search.getSearchManager((Cache<String, Song>) songCache);
		
		org.infinispan.query.dsl.Query q = sm.getQueryFactory().from(Song.class)
				.having("filePath").like("%%").toBuilder().build();
		
		logger.debug("DSL query: " + q);
		
		for (Object o : q.list()) {
			if (o instanceof Song) {
				songs.add(((Song) o));
				}
			}
		
		return songs;
		*/
		
		for (String key : songCache.keySet()){ 
			  songs.add(songCache.get(key)); 
		}
		return songs;
	}

	@Override
	public List<Song> getTop10Songs() throws CacheException {
		List<Song> songs = getAllSongs();
		Collections.sort(songs, timesPlayedComparator);
		if(songs.size() > 10) {
			return songs.subList(0, 10);
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
		
		songCache = provider.getCacheContainer().getCache(SONG_CACHE_NAME);
		
		SearchManager sm = Search.getSearchManager((Cache<String, Song>) songCache);
		
		org.infinispan.query.dsl.Query q = sm.getQueryFactory().from(Song.class)
				.having("songName").like("%"+fulltext+"%").toBuilder().build();

		return q.list();
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
			//Doesn't seem to remove data from cache store
			//songCache.clear();
		
			for (String key : songCache.keySet()){ 
				songCache.remove(key);
			}
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
	
    /**
     * {@link Song} comparator by timesPlayed.
     */
    private static Comparator<Song> timesPlayedComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return compare(s1.getTimesPlayed(), s2.getTimesPlayed());
        }
        
        public int compare(Long o1, Long o2) {
            return o1==null?Integer.MAX_VALUE:o2==null?Integer.MIN_VALUE:o2.compareTo(o1);
        }
    };
	
}
