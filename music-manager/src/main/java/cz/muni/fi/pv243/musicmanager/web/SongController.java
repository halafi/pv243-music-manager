package cz.muni.fi.pv243.musicmanager.web;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.services.SongService;

/**
 * Song JSF controller for the {@link Song} entity.
 * @author filip
 */
@Model
public class SongController {
	
	// location of the uploaded songs
	public static final String SONG_FOLDER =
			System.getProperty("user.home") + File.separator + "music-manager" + File.separator + "songs";
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private SongService songService;
	
	@Produces
	@Named
	private Song newSong;
	
	@PostConstruct
	public void initSong(){
		newSong = new Song();
	}
	
	public void addSong() {
		throw new UnsupportedOperationException();
	}
}
