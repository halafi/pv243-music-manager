package cz.muni.fi.pv243.backend.dao;

import cz.muni.fi.pv243.backend.entities.Song;

public interface SongDAO {
	
	public void createSong(Song song);
	
	public void removeSong(Song song);
	
	public void getSong(Long id);
}
