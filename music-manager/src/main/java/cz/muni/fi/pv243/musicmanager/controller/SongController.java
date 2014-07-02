package cz.muni.fi.pv243.musicmanager.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Interpret;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.FileUploadException;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.exceptions.SongFileValidationException;
import cz.muni.fi.pv243.musicmanager.services.InterpretService;
import cz.muni.fi.pv243.musicmanager.services.SongService;

/**
 * Song JSF controller for the {@link Song} entity.
 * 
 * @author filip
 */
@Model
public class SongController {

	public static final String SONG_FOLDER = System.getProperty("user.home")
			+ File.separator + "music-manager" + File.separator + "songs";

	@Inject
	private FacesContext facesContext;

	@Inject
	private ResourceBundle bundle;

	@Inject
	private SongService songService;
	@Inject
	private InterpretService interpretService;

	@Inject
	private Logger logger;

	@Produces
	@Named
	private Song newSong;

	private Part file;

	@PostConstruct
	public void initSong() {
		newSong = new Song();
		//delete
		//newSong.setId("46c7bbd9-4b70-4a1c-b655-17e4c4501ba8");
		newSong.setTimesPlayed(0);
		newSong.setComments(new ArrayList<Comment>());
		newSong.setInterpretId("Unknown");
		newSong.setUploaderUserName("Unknown");
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public Interpret[] getInterprets() {
		try {
			List<Interpret> interprets = interpretService.getAllInterprets();
			return interprets.toArray(new Interpret[interprets.size()]);
		} catch (ServiceException e) {
			return null;
		}

	}

	public String addSong() throws IOException {
		try {
			validateSong();
			uploadSong();
			newSong.setFilePath(SONG_FOLDER + File.separator
					+ getFilename(file));
			songService.createSong(newSong);
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("song.add.success"), null));
			logger.info(newSong.getSongName() + " uploaded to " + SONG_FOLDER
					+ File.separator + getFilename(file));
			initSong();
			return "index";
		} catch (SongFileValidationException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.add.fail.validation"), null));
		} catch (FileUploadException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, bundle
							.getString("song.add.fail.upload"), null));
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.add.fail.service"), null));
		} catch (Exception e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.add.fail.unknown")
							+ e.getMessage(), null));
		}
		return "";
	}

	public String udateSong() {
		try {
			songService.updateSong(newSong);
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("song.update.success"), null));
			initSong();
			return "index";
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.update.fail.service"), null));
		}
		return "";
	}
//	public String getNewSongId(){
//		return newSong.getId();
//	}
	public void playSong(String id) {
		try {
			Song song = songService.getSong(id);
			song.setTimesPlayed(song.getTimesPlayed() + 1);
			songService.updateSong(song);
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.play.fail.service")
							+ e.getMessage(), null));
		}
	}

	@Produces
	@Named
	public List<Song> getAllSongs() {
		try {
			// return songService.getSongsbyInterpret("Unknown");
			return songService.getAllSongs();
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.getAll.fail.service"), null));
		}
		return null;
	}

	public void removeSong(String id) {
		try {
			Song song = songService.getSong(id);
			File toRemove = new File(song.getFilePath());
			if (toRemove.delete()) {
				songService.removeSong(songService.getSong(id));
			}
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("song.remove.success"), null));
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.remove.fail.service"), null));
		}
	}

	public void removeAllSongs() {
		try {
			songService.removeAllSongs();
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("song.remove.success"), null));
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.remove.fail.service"), null));
		}
		try {
			FileUtils.cleanDirectory(new File(SONG_FOLDER));
		} catch (IOException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.remove.fail.io"), null));
		}
	}

	public void validateSong() throws SongFileValidationException {
		String fileName = getFilename(file);
		String fileExtension = fileName.substring(
				fileName.lastIndexOf(".") + 1, fileName.length());
		if (!fileExtension.equals("mp3")) {
			throw new SongFileValidationException("Unsupported file extension.");
		}
	}

	public void uploadSong() throws FileUploadException {
		try {
			InputStream is = file.getInputStream();
			File toFile = new File(SONG_FOLDER, getFilename(file));
			if (toFile.exists()) {
				is.close();
				throw new FileUploadException("File already exist.");
			}
			if (!toFile.getParentFile().exists()) { // create missing folder
				toFile.getParentFile().mkdirs();
			}
			FileOutputStream os = new FileOutputStream(toFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int read = -1;
			while ((read = is.read()) != -1) {
				bos.write(read);
			}
			bos.flush();
			bos.close();
			os.close();
			is.close();
		} catch (IOException ex) {
			throw new FileUploadException("File upload error.", ex);
		}
	}

	private static String getFilename(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String filename = cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
				return filename.substring(filename.lastIndexOf('/') + 1)
						.substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
			}
		}
		return null;
	}

	public String details(String id) {
		try {
			newSong = songService.getSong(id);
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.get.fail.service"), null));
		}
		return "details";
	}

	public String edit(String id) {
		try {
			newSong = songService.getSong(id);
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("song.get.fail.service"), null));
		}
		return "edit";
	}

}