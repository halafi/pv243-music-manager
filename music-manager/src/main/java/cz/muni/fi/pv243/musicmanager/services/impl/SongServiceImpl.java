package cz.muni.fi.pv243.musicmanager.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.dao.SongManager;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.SongService;

@Stateless
public class SongServiceImpl implements SongService {
	
	@Inject
	private SongManager songManager;
	
	@Inject
    private Validator validator;
	
	@Inject
	private Logger logger;
	
	// FILE UPLOAD WILL HAPPEN ON PRESENTATION LAYER
	// public static final String SONG_FOLDER = 
	// System.getProperty("user.home") + File.separator + "music-manager" + File.separator + "songs";

	@Override
	public void createSong(Song song) throws ServiceException {
		try {
			validateSong(song);
		} catch(ConstraintViolationException ex) {
			logger.debug("Failed to validate song.", ex);
			throw new ServiceException("Failed to validate song.", ex);
		}
		try {
			songManager.createSong(song);
		} catch (Exception ex) {
			logger.debug("Failed to persist song.", ex);
			throw new ServiceException("Failed to persist song.", ex);
		}
	}

	@Override
	public void updateSong(Song song) throws ServiceException {
		try {
			validateSong(song);
		} catch(ConstraintViolationException ex) {
			logger.debug("Failed to validate song.", ex);
			throw new ServiceException("Failed to validate song.", ex);
		}
		try {
			songManager.updateSong(song);
		} catch (Exception ex) {
			logger.debug("Failed to update song.", ex);
			throw new ServiceException("Failed to update song.", ex);
		}
	}

	@Override
	public void removeSong(Song song) throws ServiceException {
		try {
			validateSong(song);
		} catch(ConstraintViolationException ex) {
			logger.debug("Failed to validate song.", ex);
			throw new ServiceException("Failed to validate song.", ex);
		}
		try {
			songManager.removeSong(song);
		} catch (Exception ex) {
			logger.debug("Failed to remove song.", ex);
			throw new ServiceException("Failed to remove song.", ex);
		}
	}

	@Override
	public Song getSong(String id) throws ServiceException {
		Song song = null;
		try {
			song = songManager.getSong(id);
		} catch (Exception ex) {
			logger.debug("Failed to retrieve song with id "+id+".", ex);
			throw new ServiceException("Failed to retrieve song with id "+id+".", ex);
		}
		return song;
	}

	@Override
	public List<Song> getAllSongs() throws ServiceException {
		List<Song> songs = new ArrayList<Song>();
		try {
			songs = songManager.getAllSongs();
		} catch (Exception ex) {
			logger.debug("Failed to retrieve all songs.", ex);
			throw new ServiceException("Failed to retrieve all songs.", ex);
		}
		return songs;
	}

	@Override
	public List<Song> getTop10Songs() throws ServiceException {
		List<Song> songs = new ArrayList<Song>();
		try {
			songs = songManager.getTop10Songs();
		} catch (Exception ex) {
			logger.debug("Failed to retrieve top 10 songs.", ex);
			throw new ServiceException("Failed to retrieve top 10 songs.", ex);
		}
		return songs;
	}

	@Override
	public List<Song> getSongsbyInterpret(String interpretId) throws ServiceException {
		List<Song> songs = new ArrayList<Song>();
		try {
			songs = songManager.getSongsbyInterpret(interpretId);
			
		} catch (Exception ex) {
			logger.debug("Failed to retrieve songs by interpret.", ex);
			throw new ServiceException("Failed to retrieve songs by interpret.", ex);
		}
		return songs;
	}

	@Override
	public List<Song> searchSongs(String fulltext) throws ServiceException {
		List<Song> songs = new ArrayList<Song>();
		try {
			songs = songManager.searchSongs(fulltext);
		} catch (Exception ex) {
			logger.debug("Failed to search songs.", ex);
			throw new ServiceException("Failed to search songs.", ex);
		}
		return songs;
	}

	@Override
	public List<Song> getUserSongs(String userName) throws ServiceException {
		List<Song> songs = new ArrayList<Song>();
		try {
			songs = songManager.getUserSongs(userName);
		} catch (Exception ex) {
			logger.debug("Failed to retrieve songs by userName.", ex);
			throw new ServiceException("Failed to retrieve songs by userName.", ex);
		}
		return songs;
	}

	@Override
	public void removeAllSongs() throws ServiceException {
		try {
			songManager.removeAllSongs();
		} catch (Exception ex) {
			logger.debug("Failed to remove all songs.", ex);
			throw new ServiceException("Failed to remove all songs.", ex);
		}
	}
	
	/**
	 * Validates the given {@link Song}
	 * @param song Song to be validated
     * @throws ConstraintViolationException If Bean Validation error occurs
	 */
	private void validateSong(Song song) throws ConstraintViolationException {
		if(song == null){
			throw new IllegalArgumentException("Song is null.");
		}
		Set<ConstraintViolation<Song>> violations = validator.validate(song);
		logger.info("Validation completed. Violations found: " + violations.size());
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                new HashSet<ConstraintViolation<?>>(violations));
        }
	}
	
}
