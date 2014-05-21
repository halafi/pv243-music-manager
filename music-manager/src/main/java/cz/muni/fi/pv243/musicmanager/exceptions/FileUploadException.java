package cz.muni.fi.pv243.musicmanager.exceptions;

public class FileUploadException extends Exception {
	private static final long serialVersionUID = 1L;

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadException(String message) {
        super(message);
    }
    
    public FileUploadException(Throwable cause) {
        super(cause);
    }
}
