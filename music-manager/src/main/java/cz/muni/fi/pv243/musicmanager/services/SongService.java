package cz.muni.fi.pv243.musicmanager.services;

import java.util.List;

import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;

/**
 * Service layer for {@link Song} entity manipulation using SongManager.
 * @author filip
 */
public interface SongService {
	
	public void createSong(Song song) throws ServiceException;
	
	public void updateSong(Song song) throws ServiceException;
	
	public void removeSong(Song song) throws ServiceException;
	
	public Song getSong(String id) throws ServiceException;
	
	public List<Song> getAllSongs() throws ServiceException;
	
	public List<Song> getTop10Songs() throws ServiceException;
	
	public List<Song> getSongsbyInterpret(String interpretId) throws ServiceException;
	
	public List<Song> searchSongs(String fulltext) throws ServiceException;
	
	public List<Song> getUserSongs(String userName) throws ServiceException;
	
	public void removeAllSongs() throws ServiceException;
}
