package cz.muni.fi.pv243.musicmanager.services;

import java.util.List;

import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;

/**
 * Service layer for {@link Song} entity manipulation using SongManager.
 * @author filip
 */
public interface SongService {
	
	/**
	 * Validates and creates a new {@link Song}.
	 * @param song song to be created
	 * @throws ServiceException when any exception occurs on persistence layer or on song validation error
	 */
	public void createSong(Song song) throws ServiceException;
	
	/**
	 * Validates and updates a {@link Song}.
	 * @param song song to be updated
	 * @throws ServiceException when any exception occurs on persistence layer or on song validation error
	 */
	public void updateSong(Song song) throws ServiceException;
	
	/**
	 * Removes a {@link Song}.
	 * @param song song to be removed
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public void removeSong(Song song) throws ServiceException;
	
	/**
	 * Retrieves a {@link Song} with the given id.
	 * @param id id of the song to be retrieved
	 * @return song with the given id or null when the song does not exist
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public Song getSong(String id) throws ServiceException;
	
	/**
	 * Retrieves every {@link Song} from the cache.
	 * @return list of every song
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public List<Song> getAllSongs() throws ServiceException;
	
	/**
	 * Retrieves a list of each {@link Song} that is in the top 10 most played (determined by timesPlayed), null if there are not any songs.
	 * @return list of top 10 songs most played or null
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public List<Song> getTop10Songs() throws ServiceException;
	
	/**
	 * Retrieves a list of each {@link Song} for the given interpret, null if there are not any songs.
	 * @param interpretId id of the desired interpret
	 * @return list of all interpret songs or null
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public List<Song> getSongsbyInterpret(String interpretId) throws ServiceException;
	
	/**
	 * Retrieves a list of each {@link Song} containing the search string in the song name.
	 * @param fulltext string to be searched for as a factor of song name
	 * @return list of all songs with the factor fulltext in the song name
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public List<Song> searchSongs(String fulltext) throws ServiceException;
	
	/**
	 * Retrieves a list of each {@link Song} uploaded by the given user, null if there are not any songs.
	 * @param userName id of the user
	 * @return list of all songs of the user or null
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public List<Song> getUserSongs(String userName) throws ServiceException;
	
	/**
	 * Removes all {@link Song} entities from the cache.
	 * @throws ServiceException when any exception occurs on persistence layer
	 */
	public void removeAllSongs() throws ServiceException;
}
