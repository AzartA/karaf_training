package ru.training.karaf.exception;

public class RestrictedException extends RuntimeException{
    public RestrictedException() {
        super();
    }

    public RestrictedException(String message) {
        super(message);
    }

    public RestrictedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestrictedException(Throwable cause) {
        super(cause);
    }

    protected RestrictedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
