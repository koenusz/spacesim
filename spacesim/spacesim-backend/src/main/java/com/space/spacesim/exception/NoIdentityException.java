package com.space.spacesim.exception;

public class NoIdentityException extends RuntimeException {

	
	private static final long serialVersionUID = 7729565354631610267L;

	public NoIdentityException() {
		super("No identity found for this entity, This means the entity is not properly registered with the database");
	}

	public NoIdentityException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoIdentityException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NoIdentityException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoIdentityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
