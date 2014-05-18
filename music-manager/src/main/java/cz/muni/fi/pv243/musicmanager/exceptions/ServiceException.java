package cz.muni.fi.pv243.musicmanager.exceptions;

/**
 * Service exception that wraps all service layer exceptions occurred.
 * */
public class ServiceException extends Exception{
	private static final long serialVersionUID = 1L;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
