package cz.muni.fi.pv243.musicmanager.exceptions;

public class SongFileValidationException extends Exception {
	private static final long serialVersionUID = 1L;

    public SongFileValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SongFileValidationException(String message) {
        super(message);
    }
    
    public SongFileValidationException(Throwable cause) {
        super(cause);
    }
}
