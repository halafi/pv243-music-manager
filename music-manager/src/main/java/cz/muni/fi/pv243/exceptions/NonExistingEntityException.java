package cz.muni.fi.pv243.exceptions;

public class NonExistingEntityException extends Exception{
	private static final long serialVersionUID = 1L;

    public NonExistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistingEntityException(String message) {
        super(message);
    }
}
