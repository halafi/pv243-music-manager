package cz.muni.fi.pv243.exceptions;

public class EntityExistsException extends Exception{
	private static final long serialVersionUID = 1L;

    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityExistsException(String message) {
        super(message);
    }

}
