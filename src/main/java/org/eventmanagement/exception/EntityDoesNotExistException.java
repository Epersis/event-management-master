package org.eventmanagement.exception;

public class EntityDoesNotExistException extends Exception {
    public EntityDoesNotExistException(String s) {
    }

    public EntityDoesNotExistException() {
        super();
    }

    public EntityDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityDoesNotExistException(Throwable cause) {
        super(cause);
    }

    protected EntityDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
