package cz.muni.fi.pv243.musicmanager.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.FileUploadException;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.exceptions.SongFileValidationException;
import cz.muni.fi.pv243.musicmanager.services.SongService;

/**
 * Song JSF controller for the {@link Song} entity.
 * @author filip
 */
@Model
public class SongController {
	
	public static final String SONG_FOLDER =
			System.getProperty("user.home") + File.separator + "music-manager";
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private SongService songService;
	
	@Inject
	private Logger logger;
	
	@Produces
	@Named
	private Song newSong;
	
	@PostConstruct
	public void initSong(){
		newSong = new Song();
		newSong.setTimesPlayed(0);
		newSong.setComments(new ArrayList<Comment>());
		newSong.setInterpretId("Unknown");
		newSong.setUploaderUserName("Unknown");
	}
	
	private Part file;
	
	public Part getFile() {
		return file;
	}
		 
	public void setFile(Part file) {
		this.file = file;
	}
	
	public void addSong() throws IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("i18n.jsf.Messages", facesContext.getViewRoot().getLocale());
		try {
			validateSong();
			uploadSong();		
			newSong.setFilePath(SONG_FOLDER + File.separator + getFilename(file));
			songService.createSong(newSong);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
					bundle.getString("song.add.success"), null));
			logger.info("Song uploaded to: " + SONG_FOLDER + File.separator + getFilename(file));
			initSong();
		} catch (SongFileValidationException ex) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("song.add.fail.validation"), null));
		} catch (FileUploadException ex) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					bundle.getString("song.add.fail.upload"), null));
		} catch (ServiceException ex) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("song.add.fail.service"), null));
		} catch (Exception ex) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("song.add.fail.unknown") + ex.getMessage(), null));
		}
	}
	
    public void validateSong() throws SongFileValidationException {
    	String fileName = getFilename(file);
    	String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    	if(!fileExtension.equals("mp3")) {
            throw new SongFileValidationException("Unsupported file extension.");
        }
    }
    
    public void uploadSong() throws FileUploadException {
    	try {
	    	InputStream is = file.getInputStream();
	    	File toFile = new File(SONG_FOLDER, getFilename(file));
	    	if(toFile.exists()) {
	    		is.close();
	    		throw new FileUploadException("File already exist.");
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
    
    private static String getFilename (Part part) {
    	for (String cd : part.getHeader("content-disposition").split(";")) {
    		if (cd.trim().startsWith("filename")) {
    			String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
    			return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
    			}
    		}
    	return null;
    }
}