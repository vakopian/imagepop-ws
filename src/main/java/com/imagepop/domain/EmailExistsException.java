package com.imagepop.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by BernardXie on 3/21/16.
 */

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicate Emails")
public final class EmailExistsException extends RuntimeException {
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
