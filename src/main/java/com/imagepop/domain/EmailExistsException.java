package com.imagepop.domain;

/**
 * Created by BernardXie on 3/21/16.
 */
public final class EmailExistsException extends RuntimeException{
    public EmailExistsException() {
        super();
    }

    public EmailExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmailExistsException(final String message) {
        super(message);
    }

    public EmailExistsException(final Throwable cause) {
        super(cause);
    }
}
