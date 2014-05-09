package cz.muni.fi.pv243.backend.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.Cacheable;

import org.infinispan.commons.api.BasicCache;

import cz.muni.fi.pv243.backend.dao.SongManager;
import cz.muni.fi.pv243.backend.entities.Song;
import cz.muni.fi.pv243.exceptions.EntityExistsException;
import cz.muni.fi.pv243.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.utils.UUIDStringGenerator;

/**
 * 
 * @author filip
 */
public class SongManagerImpl implements SongManager {
	
	@Inject
	private CacheContainerProvider provider;
	
	private BasicCache<String, Song> songCache = provider.getCacheContainer().getCache("songcache");

	@Override
	//@CachePut(cacheName="songCache")
	public void createSong(Song song) throws EntityExistsException,
			IllegalArgumentException {
		if (song == null) {
			throw new IllegalArgumentException("Song argument is null");
		}
		
		if (song.getId() != null) {
			throw new IllegalArgumentException("Song has already assigned id.");
		} else {
			song.setId(UUIDStringGenerator.generateSongId());
			songCache.put(song.getId(), song);
		}
	}

	@Override
	public void removeSong(Song song) throws NonExistingEntityException,
			IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Song getSong(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Song> getAllSongs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Song> getTop10Songs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Song> getSongsbyInterpret(String interpretId)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Song> getUserSongs(String userName)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
