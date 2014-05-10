package cz.muni.fi.pv243.musicmanager.dao;

import java.util.List;

import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;

/**
 * Interface for {@link Song} entity manipulation using the infinispan cache.
 * @author filip
 */
public interface SongManager {
	
	/**
	 * Creates a new {@link Song}.
	 * @param song song to be added
	 * @throws IllegalArgumentException if song is null
	 * @throws IllegalEntityException if the song is invalid
	 */
	public void createSong(Song song) throws IllegalEntityException, IllegalArgumentException;
	
	/**
	 * Removes a given {@link Song}.
	 * @param song song to be removed
	 * @throws NonExistingEntityException if song does not exist and cannot be removed
	 * @throws IllegalArgumentException if song is null
	 */
	public void removeSong(Song song) throws NonExistingEntityException, IllegalArgumentException;

	/**
	 * Retrieves a {@link Song} with the given id.
	 * @param id id of the song to be retrieved
	 * @return song with the given id or null when the song does not exist
	 * @throws IllegalArgumentException if id is null
	 */
	public Song getSong(String id);
	
	/**
	 * Retrieves a list of each {@link Song} in the cache, null if there are not any songs.
	 * @return list of each song in the cache or null
	 */
	public List<Song> getAllSongs();
	
	/**
	 * Retrieves a list of each {@link Song} that is in the top 10 most played (determined by timesPlayed), null if there are not any songs.
	 * @return list of top 10 songs most played or null
	 */
	public List<Song> getTop10Songs();
	
	/**
	 * Retrieves a list of each {@link Song} for the given interpret, null if there are not any songs.
	 * @param interpretId id of the desired interpret
	 * @return list of all interpret songs or null
	 * @throws IllegalArgumentException if interpretId is null
	 */
	public List<Song> getSongsbyInterpret(String interpretId) throws IllegalArgumentException;
	
	/**
	 * Retrieves a list of each {@link Song} containing the factor in the song name.
	 * @param factor factor string to be searched for
	 * @return list of all songs with the factor string in the song name
	 * @throws IllegalArgumentException if factor is null or empty
	 */
	public List<Song> searchSongsByName(String factor) throws IllegalArgumentException;
	
	/**
	 * Retrieves a list of each {@link Song} uploaded by the given user, null if there are not any songs.
	 * @param userName id of the user
	 * @return list of all songs of the user or null
	 * @throws IllegalArgumentException if userName is null
	 */
	public List<Song> getUserSongs(String userName) throws IllegalArgumentException;
	
}
